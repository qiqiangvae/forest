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
public class ConditionUtils {

    public static Map<Field, List<ConditionWrapper>> parseQueryParam(QueryParam queryParam) {
        Class<? extends QueryParam> paramClass = queryParam.getClass();
        Set<Field> fields = FieldUtils.getAllFields(paramClass);
        Map<Field, List<ConditionWrapper>> conditionMap = new LinkedHashMap<>(fields.size());
        for (Field field : fields) {
            Condition[] conditions = AnnotationUtils.getAnnotations(field, Condition.class);
            conditionMap.put(field,convertConditions(conditions));
        }
        return conditionMap;
    }

    private static List<ConditionWrapper> convertConditions(Condition[] conditions) {
        return Stream.of(conditions)
                .map(condition -> new ConditionWrapper().setExpress(condition.express()).setCol(condition.col()))
                .collect(Collectors.toList());
    }
}
