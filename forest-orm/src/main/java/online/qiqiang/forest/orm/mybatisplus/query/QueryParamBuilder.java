package online.qiqiang.forest.orm.mybatisplus.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import online.qiqiang.forest.common.utils.reflection.AnnotationUtils;
import online.qiqiang.forest.common.utils.reflection.PropertyUtils;
import online.qiqiang.forest.query.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author qiqiang
 */
public class QueryParamBuilder extends AbstractParamBuilder {

    private static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class)
            .configure().failFast(true).buildValidatorFactory()
            .getValidator();

    public static <T> Page<T> toPage(PageQuery pageQuery) {
        int[] paging = pageQuery.getPaging();
        Page<T> page = new Page<>(paging[0], paging[1]);
        List<SortColumn> sorts = pageQuery.getSorts();
        // 从缓存中获取
        sorts.addAll(getSorts(pageQuery));
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
        // 构建查询结果
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(queryParam.getSelectList())) {
            queryWrapper.select(queryParam.getSelectList().toArray(new String[0]));
        }
        // 构建查询条件
        iterate(queryParam.getClass(), (field, condition) -> {
            // 空值
            final Object value = PropertyUtils.getValue(field, queryParam);
            if (condition.isIgnoreEmpty() && ConditionWrapper.ignore(value)) {
                return;
            }
            String col = condition.getCol();
            if (StringUtils.isBlank(col)) {
                TableField tableField = AnnotationUtils.getAnnotation(field, TableField.class);
                if (tableField != null) {
                    col = tableField.value();
                }
                if (StringUtils.isBlank(col)) {
                    col = field.getName();
                }
            }
            Express express = condition.getExpress();
            buildQueryWrapper(queryParam.getClass(), queryWrapper, field.getName(), value, col, express);
        });
        return queryWrapper;
    }

    private static <T> void buildQueryWrapper(Class<? extends AbstractQueryParam> queryParamClass, QueryWrapper<T> queryWrapper, String fieldName, Object value, String col, Express express) {
        switch (express) {
            case is_null:
                queryWrapper.isNull(col);
                break;
            case not_null:
                queryWrapper.isNotNull(col);
                break;
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
                    throw new QueryBuildForestException(queryParamClass + "." + fieldName + "必须是集合或数组类型");
                }
                break;
            case not_in:
                if (value instanceof Collection || value.getClass().isArray()) {
                    queryWrapper.notIn(col, value);
                } else {
                    throw new QueryBuildForestException(queryParamClass + "." + fieldName + "必须是集合或数组类型");
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
                queryWrapper.like(col, "%" + value + "%");
                break;
            case left_like:
                queryWrapper.likeLeft(col, "%" + value);
                break;
            case right_like:
                queryWrapper.likeRight(col, value + "%");
                break;
            case between:
                Object[] arr = betweenValue(fieldName, value);
                queryWrapper.between(col, arr[0], arr[1]);
                break;
            default:
                throw new QueryBuildForestException(fieldName + "的条件不合法");
        }
    }
}
