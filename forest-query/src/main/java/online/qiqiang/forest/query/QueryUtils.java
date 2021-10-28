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

    public static Map<Field, ConditionWrapper> parseQueryParam(QueryParam queryParam) {
        Class<? extends QueryParam> paramClass = queryParam.getClass();
        Set<Field> fields = FieldUtils.getAllFields(paramClass);
        Map<Field, ConditionWrapper> conditionMap = new LinkedHashMap<>(fields.size());
        for (Field field : fields) {
            Condition condition = AnnotationUtils.getAnnotation(field, Condition.class);
            if (condition != null) {
                conditionMap.put(field, new ConditionWrapper(condition.express(), condition.col()));
            }
        }
        return conditionMap;
    }

    public static Map<Field, SortColumn> parseSorts(QueryParam queryParam) {
        Class<? extends QueryParam> paramClass = queryParam.getClass();
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
