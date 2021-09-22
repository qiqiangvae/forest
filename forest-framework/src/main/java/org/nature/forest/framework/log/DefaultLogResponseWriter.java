package org.nature.forest.framework.log;

import java.util.Map;

/**
 * @author qiqiang
 */
public class DefaultLogResponseWriter implements LogResponseWriter {

    @Override
    public Map<String, Object> write(Object result, Map<String, Object> jsonMap) {
        return jsonMap;
    }
}
