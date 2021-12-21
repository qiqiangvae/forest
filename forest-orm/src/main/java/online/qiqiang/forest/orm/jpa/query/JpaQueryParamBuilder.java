package online.qiqiang.forest.orm.jpa.query;

import online.qiqiang.forest.common.utils.reflection.PropertyUtils;
import online.qiqiang.forest.query.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author qiqiang
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class JpaQueryParamBuilder extends AbstractParamBuilder {

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
        return (root, query, cb) -> {
            //用于添加所有查询条件
            List<Predicate> predicateList = new ArrayList<>();
            Class<? extends AbstractQueryParam> queryParamClass = queryParam.getClass();
            // 从缓存中获取
            iterate(queryParamClass, (field, condition) -> {
                // 空值
                final Object value = PropertyUtils.getValue(field, queryParam);
                if (condition.isIgnoreEmpty() && ConditionWrapper.ignore(value)) {
                    return;
                }
                String col = condition.getCol();
                if (StringUtils.isBlank(col)) {
                    col = field.getName();
                }
                Express express = condition.getExpress();
                buildQueryWrapper(queryParamClass, predicateList, cb, root, col, value, express);
            });
            Predicate[] pre = new Predicate[predicateList.size()];
            Predicate and = cb.and(predicateList.toArray(pre));
            query.where(and);
            //设置排序
            List<Order> orders = sort(queryParam, root, cb);
            return query.orderBy(orders).getRestriction();
        };
    }

    private static <T> List<Order> sort(AbstractQueryParam queryParam, Root<T> root, CriteriaBuilder cb) {
        List<SortColumn> sorts = queryParam.getSorts();
        // 从缓存中获取
        sorts.addAll(getSorts(queryParam));
        List<Order> orders = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sorts)) {
            for (SortColumn sort : sorts) {
                if (SortColumn.Sort.Asc.equals(sort.getSort())) {
                    orders.add(cb.asc(root.get(sort.getColumn())));
                } else if (SortColumn.Sort.Desc.equals(sort.getSort())) {
                    orders.add(cb.desc(root.get(sort.getColumn())));
                }
            }
        }
        return orders;
    }

    private static <T> void buildQueryWrapper(Class<? extends AbstractQueryParam> queryParamClass, List<Predicate> predicates, CriteriaBuilder builder, Path<T> root, String fieldName, Object value, Express express) {
        Predicate predicate;
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
                    predicate = builder.in(root.get(fieldName)).in(value).not();
                } else {
                    throw new QueryBuildForestException(queryParamClass + "." + fieldName + "必须是集合或数组类型");
                }
                break;
            case gt:
                predicate = builder.greaterThan(root.get(fieldName), (Comparable) value);
                break;
            case gte:
                predicate = builder.greaterThanOrEqualTo(root.get(fieldName), (Comparable) value);
                break;
            case lt:
                predicate = builder.lessThan(root.get(fieldName), (Comparable) value);
                break;
            case lte:
                predicate = builder.lessThanOrEqualTo(root.get(fieldName), (Comparable) value);
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
                Object[] arr = betweenValue(fieldName, value);
                predicate = builder.between(root.get(fieldName), (Comparable) arr[0], (Comparable) arr[1]);
                break;
            default:
                throw new QueryBuildForestException(fieldName + "的条件不合法");
        }
        predicates.add(predicate);
    }
}
