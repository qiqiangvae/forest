package org.qiqiang.forest.query;

import org.qiqiang.forest.common.utils.reflection.AnnotationUtils;
import org.qiqiang.forest.common.utils.reflection.FieldUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiqiang
 */
public class QueryUtils {

    public static Map<Field, List<ConditionWrapper>> parseQueryParam(QueryParam queryParam) {
        Class<? extends QueryParam> paramClass = queryParam.getClass();
        Set<Field> fields = FieldUtils.getAllFields(paramClass);
        Map<Field, List<ConditionWrapper>> conditionMap = new LinkedHashMap<>(fields.size());
        for (Field field : fields) {
            Condition[] conditions = AnnotationUtils.getAnnotations(field, Condition.class);
            conditionMap.put(field, convertConditions(conditions));
        }
        return conditionMap;
    }

    private static List<ConditionWrapper> convertConditions(Condition[] conditions) {
        return Stream.of(conditions)
                .map(condition -> new ConditionWrapper(condition.express(), condition.col()))
                .collect(Collectors.toList());
    }

    public static Map<Field, SortColumn> parseSorts(QueryParam queryParam) {
        Class<? extends QueryParam> paramClass = queryParam.getClass();
        Set<Field> fields = FieldUtils.getAllFields(paramClass);
        Map<Field, SortColumn> sortMap = new LinkedHashMap<>(fields.size());
        for (Field field : fields) {
            Sort sort = AnnotationUtils.getAnnotation(field, Sort.class);
            sortMap.put(field, new SortColumn(sort.col(), sort.sort()));
        }
        return sortMap;
    }
}
