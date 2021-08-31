package org.qiqiang.forest.mybatisplus.query;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.qiqiang.forest.common.utils.reflection.PropertyUtils;
import org.qiqiang.forest.query.ConditionUtils;
import org.qiqiang.forest.query.ConditionWrapper;
import org.qiqiang.forest.query.Express;
import org.qiqiang.forest.query.QueryParam;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiqiang
 */
public abstract class AbstractQueryParam<T> implements QueryParam {

    private static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class)
            .configure().failFast(true).buildValidatorFactory()
            .getValidator();

    private List<String> selectList;

    public <S extends AbstractQueryParam<T>> S select(String... col) {
        if (col != null && col.length > 0) {
            this.selectList = Stream.of(col).collect(Collectors.toList());
        }
        return (S) this;
    }

    public Wrapper<T> toWrapper() {
        // 校验
        Set<ConstraintViolation<AbstractQueryParam<T>>> violations = VALIDATOR.validate(this);
        if (CollectionUtils.isNotEmpty(violations)) {
            throw new QueryBuildForestException(violations.toString());
        }
        // 构建
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(selectList)) {
            queryWrapper.select(selectList.toArray(new String[0]));
        }
        Map<Field, List<ConditionWrapper>> conditionMap = ConditionUtils.parseQueryParam(this);
        for (Map.Entry<Field, List<ConditionWrapper>> entry : conditionMap.entrySet()) {
            final Field field = entry.getKey();
            final String fieldName = field.getName();
            final Object value = PropertyUtils.getValue(field, this);
            if (value == null) {
                continue;
            }
            for (ConditionWrapper condition : entry.getValue()) {
                String col = condition.getCol();
                if (StringUtils.isBlank(col)) {
                    col = fieldName;
                }
                Express express = condition.getExpress();
                buildQueryWrapper(queryWrapper, fieldName, value, col, express);
            }
        }
        return queryWrapper;
    }

    private void buildQueryWrapper(QueryWrapper<T> queryWrapper, String fieldName, Object value, String col, Express express) {
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
                    throw new QueryBuildForestException(this.getClass() + "." + fieldName + "必须是集合或数组类型");
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
