package online.qiqiang.forest.query;

import online.qiqiang.forest.common.utils.reflection.AnnotationUtils;
import online.qiqiang.forest.common.utils.reflection.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author qiqiang
 */
public class QueryUtils {

    public static Map<Field, ConditionWrapper> parseQueryParam(Class<? extends QueryParam> paramClass) {
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

    public static Map<Field, SortColumn> parseSorts(Class<? extends QueryParam> paramClass) {
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
}
