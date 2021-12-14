package online.qiqiang.forest.orm.jpa.query;

import com.baomidou.mybatisplus.annotation.TableField;
import online.qiqiang.forest.common.utils.reflection.AnnotationUtils;
import online.qiqiang.forest.common.utils.reflection.PropertyUtils;
import online.qiqiang.forest.orm.QueryBuildForestException;
import online.qiqiang.forest.orm.QueryConst;
import online.qiqiang.forest.query.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiqiang
 */
public class JpaQueryParamBuilder {

    private static final Map<Class<? extends AbstractQueryParam>, Map<Field, ConditionWrapper>> CONDITION_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<? extends QueryParam>, Map<Field, SortColumn>> SORT_CACHE = new ConcurrentHashMap<>();

    private static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class)
            .configure().failFast(true).buildValidatorFactory()
            .getValidator();

    public static <T> Pageable toPage(PageQuery pageQuery) {
        return PageRequest.of(pageQuery.getCurrent(), pageQuery.getPageSize());
    }

    public static <T> Specification<T> toWrapper(AbstractQueryParam queryParam) {
        // 校验
        Set<ConstraintViolation<AbstractQueryParam>> violations = VALIDATOR.validate(queryParam);
        if (CollectionUtils.isNotEmpty(violations)) {
            throw new QueryBuildForestException(violations.toString());
        }
        return (Specification<T>) (root, query, cb) -> {
            //用于添加所有查询条件
            List<Predicate> p = new ArrayList<>();
            Class<? extends AbstractQueryParam> queryParamClass = queryParam.getClass();
            // 从缓存中获取
            Map<Field, ConditionWrapper> conditionMap = CONDITION_CACHE.computeIfAbsent(queryParamClass, QueryUtils::parseQueryParam);
            for (Map.Entry<Field, ConditionWrapper> entry : conditionMap.entrySet()) {
                final Field field = entry.getKey();
                final String fieldName = field.getName();
                ConditionWrapper condition = entry.getValue();
                // 空值
                final Object value = PropertyUtils.getValue(field, queryParam);
                if (condition.isIgnoreEmpty()) {
                    if (value == null) {
                        continue;
                    }
                    if (value instanceof String && StringUtils.isBlank((String) value)) {
                        continue;
                    }
                    if (value instanceof Collection && CollectionUtils.isEmpty((Collection<?>) value)) {
                        continue;
                    }
                }
                String col = condition.getCol();
                if (StringUtils.isBlank(col)) {
                    TableField tableField = AnnotationUtils.getAnnotation(field, TableField.class);
                    if (tableField != null) {
                        col = tableField.value();
                    }
                    if (StringUtils.isBlank(col)) {
                        col = fieldName;
                    }
                }
                Express express = condition.getExpress();
                buildQueryWrapper(queryParamClass, p, cb, root, fieldName, value, express);
            }
            Predicate[] pre = new Predicate[p.size()];
            Predicate and = cb.and(p.toArray(pre));
            query.where(and);
            //设置排序
            List<Order> orders = new ArrayList<>();
            orders.add(cb.desc(root.get("age")));       //倒序
            orders.add(cb.asc(root.get("username")));   //正序
            return query.orderBy(orders).getRestriction();
        };
    }

    private static <T> void buildQueryWrapper(Class<? extends AbstractQueryParam> queryParamClass, List<Predicate> predicates, CriteriaBuilder builder, Path<T> root, String fieldName, Object value, Express express) {
        Predicate predicate = null;
        switch (express) {
            case is_null:
                predicate = builder.isNull(root.get(fieldName));
                break;
            case not_null:
                predicate = builder.isNotNull(root.get(fieldName));
                break;
            case equals:
                predicate = builder.equal(root.get(fieldName), value);
                break;
            case not_equals:
                predicate = builder.notEqual(root.get(fieldName), value);
                break;
            case in:
                if (value instanceof Collection || value.getClass().isArray()) {
                    predicate = builder.in(root.get(fieldName).in(value));
                } else {
                    throw new QueryBuildForestException(queryParamClass + "." + fieldName + "必须是集合或数组类型");
                }
                break;
            case not_in:
                if (value instanceof Collection || value.getClass().isArray()) {
                    predicate = builder.isNotMember(value, root.get(fieldName));
                } else {
                    throw new QueryBuildForestException(queryParamClass + "." + fieldName + "必须是集合或数组类型");
                }
                break;
            case gt:
                if (value instanceof Number) {
                    predicate = builder.gt(root.get(fieldName).as(Number.class), (Number) value);
                }
                break;
            case gte:
                if (value instanceof Number) {
                    predicate = builder.ge(root.get(fieldName).as(Number.class), (Number) value);
                }
                break;
            case lt:
                if (value instanceof Number) {
                    predicate = builder.lt(root.get(fieldName).as(Number.class), (Number) value);
                }
                break;
            case lte:
                if (value instanceof Number) {
                    predicate = builder.le(root.get(fieldName).as(Number.class), (Number) value);
                }
                break;
            case like:
                predicate = builder.like(root.get(fieldName), "%" + value + "%");
                break;
            case left_like:
                predicate = builder.like(root.get(fieldName), "%" + value);
                break;
            case right_like:
                predicate = builder.like(root.get(fieldName), value + "%");
                break;
            case between:
                Object[] arr;
                if (value.getClass().isArray()) {
                    arr = (Object[]) value;
                    if (arr.length != QueryConst.BETWEEN_VALUE_LENGTH) {
                        throw new QueryBuildForestException(fieldName + "的长度必须是2");
                    }
                } else if (value instanceof Collection) {
                    Collection<?> collections = (Collection<?>) value;
                    if (collections.size() != QueryConst.BETWEEN_VALUE_LENGTH) {
                        throw new QueryBuildForestException(fieldName + "的长度必须是2");
                    }
                    arr = collections.toArray();
                } else {
                    throw new QueryBuildForestException(fieldName + "必须是元素为2的集合或数组");
                }
                // predicate = builder.between(root.get(fieldName), arr[0], arr[1]);
                break;
            default:
                throw new QueryBuildForestException(fieldName + "的条件不合法");
        }
        predicates.add(predicate);
    }
}
