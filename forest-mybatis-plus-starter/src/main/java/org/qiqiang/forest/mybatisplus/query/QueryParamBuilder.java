package org.qiqiang.forest.mybatisplus.query;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.qiqiang.forest.common.utils.reflection.PropertyUtils;
import org.qiqiang.forest.query.ConditionWrapper;
import org.qiqiang.forest.query.Express;
import org.qiqiang.forest.query.QueryUtils;
import org.qiqiang.forest.query.SortColumn;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qiqiang
 */
public class QueryParamBuilder {

    private static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class)
            .configure().failFast(true).buildValidatorFactory()
            .getValidator();

    public static <T> Page<T> toPage(AbstractQueryParam queryParam) {
        int[] paging = queryParam.getPaging();
        Page<T> page = new Page<>(paging[0], paging[1]);
        List<SortColumn> sorts = queryParam.getSorts();
        Map<Field, SortColumn> sortMap = QueryUtils.parseSorts(queryParam);
        for (Map.Entry<Field, SortColumn> entry : sortMap.entrySet()) {
            SortColumn value = entry.getValue();
            if (StringUtils.isBlank(value.getColumn())) {
                value.setColumn(entry.getKey().getName());
            }
        }
        sorts.addAll(sortMap.values());
        if (CollectionUtils.isNotEmpty(sorts)) {
            List<OrderItem> orderItems = sorts.stream()
                    .sorted()
                    .map(
                            orderColumn -> new OrderItem(orderColumn.getColumn(), orderColumn.getSort().equals(SortColumn.Sort.Asc))
                    )
                    .collect(Collectors.toList());
            page.setOrders(orderItems);
        }
        return page;
    }

    public static <T> Wrapper<T> toWrapper(AbstractQueryParam queryParam) {
        // 校验
        Set<ConstraintViolation<AbstractQueryParam>> violations = VALIDATOR.validate(queryParam);
        if (CollectionUtils.isNotEmpty(violations)) {
            throw new QueryBuildForestException(violations.toString());
        }
        // 构建
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(queryParam.getSelectList())) {
            queryWrapper.select(queryParam.getSelectList().toArray(new String[0]));
        }
        Map<Field, ConditionWrapper> conditionMap = QueryUtils.parseQueryParam(queryParam);
        for (Map.Entry<Field, ConditionWrapper> entry : conditionMap.entrySet()) {
            final Field field = entry.getKey();
            final String fieldName = field.getName();
            final Object value = PropertyUtils.getValue(field, queryParam);
            if (value == null) {
                continue;
            }
            ConditionWrapper condition = entry.getValue();
            String col = condition.getCol();
            if (StringUtils.isBlank(col)) {
                col = fieldName;
            }
            Express express = condition.getExpress();
            buildQueryWrapper(queryParam, queryWrapper, fieldName, value, col, express);
        }
        return queryWrapper;
    }

    private static <T> void buildQueryWrapper(AbstractQueryParam queryParam, QueryWrapper<T> queryWrapper, String fieldName, Object value, String col, Express express) {
        switch (express) {
            case equals:
                queryWrapper.eq(col, value);
                break;
            case not_equals:
                queryWrapper.ne(col, value);
                break;
            case in:
                if (value instanceof Collection || value.getClass().isArray()) {
                    queryWrapper.in(col, value);
                } else {
                    throw new QueryBuildForestException(queryParam.getClass() + "." + fieldName + "必须是集合或数组类型");
                }
                break;
            case gt:
                queryWrapper.gt(col, value);
                break;
            case gte:
                queryWrapper.ge(col, value);
                break;
            case lt:
                queryWrapper.lt(col, value);
                break;
            case lte:
                queryWrapper.le(col, value);
                break;
            case like:
                queryWrapper.like(col, value);
                break;
            case left_like:
                queryWrapper.likeLeft(col, "%" + value);
                break;
            case right_like:
                queryWrapper.likeRight(col, value + "%");
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
                queryWrapper.between(col, arr[0], arr[1]);
                break;
            default:
                throw new QueryBuildForestException(fieldName + "的条件不合法");
        }
    }
}
