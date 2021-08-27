package org.qiqiang.forest.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiqiang
 */
public class ConditionUtils {

    public static List<ConditionWrapper> parseQueryParam(QueryParam queryParam){
        Class<? extends QueryParam> paramClass = queryParam.getClass();
        return new ArrayList<>();
    }
}
