package online.qiqiang.forest.query;

import online.qiqiang.forest.common.utils.reflection.AnnotationUtils;
import online.qiqiang.forest.common.utils.reflection.FieldUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

/**
 * @author qiqiang
 */
public abstract class AbstractParamBuilder {

    protected static final Map<Class<? extends AbstractQueryParam>, Map<Field, ConditionWrapper>> CONDITION_CACHE = new ConcurrentHashMap<>();
    protected static final Map<Class<? extends QueryParam>, Map<Field, SortColumn>> SORT_CACHE = new ConcurrentHashMap<>();


    protected static Object[] betweenValue(String fieldName, Object value) {
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
        return arr;
    }

    protected static Collection<SortColumn> getSorts(PageQuery pageQuery) {
        // 从缓存中获取
        Map<Field, SortColumn> sortMap = SORT_CACHE.computeIfAbsent(pageQuery.getClass(), AbstractParamBuilder::parseSorts);
        for (Map.Entry<Field, SortColumn> entry : sortMap.entrySet()) {
            SortColumn value = entry.getValue();
            if (StringUtils.isBlank(value.getColumn())) {
                value.setColumn(entry.getKey().getName());
            }
        }
        return sortMap.values();
    }

    protected static Map<Field, ConditionWrapper> parseQueryParam(Class<? extends QueryParam> paramClass) {
        Set<Field> fields = FieldUtils.getAllFields(paramClass);
        Map<Field, ConditionWrapper> conditionMap = new LinkedHashMap<>(fields.size());
        for (Field field : fields) {
            Condition condition = AnnotationUtils.getAnnotation(field, Condition.class);
            if (condition != null) {
                ConditionWrapper conditionWrapper = ConditionWrapper.builder()
                        .express(condition.express())
                        .col(condition.col())
                        .ignoreEmpty(condition.ignoreEmpty())
                        .build();
                conditionMap.put(field, conditionWrapper);
            }
        }
        return conditionMap;
    }

    private static Map<Field, SortColumn> parseSorts(Class<? extends QueryParam> paramClass) {
        Set<Field> fields = FieldUtils.getAllFields(paramClass);
        Map<Field, SortColumn> map = new HashMap<>(fields.size());
        for (Field field : fields) {
            Sort sort = AnnotationUtils.getAnnotation(field, Sort.class);
            if (sort != null) {
                map.put(field, new SortColumn(sort.col(), sort.sort(), sort.order()));
            }
        }
        return map;
    }

    protected static void iterate(Class<? extends AbstractQueryParam> queryParamClass, BiConsumer<Field, ConditionWrapper> consumer) {
        Map<Field, ConditionWrapper> conditionMap = CONDITION_CACHE.computeIfAbsent(queryParamClass, AbstractParamBuilder::parseQueryParam);
        conditionMap.forEach(consumer);
    }

}
