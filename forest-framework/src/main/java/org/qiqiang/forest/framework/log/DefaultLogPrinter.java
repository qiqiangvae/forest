package org.nature.forest.framework.log;

import org.nature.forest.common.utils.JsonUtils;

import java.util.Map;

/**
 * @author qiqiang
 */
public class DefaultLogPrinter implements LogPrinterFunction {
    @Override
    public Map<String, Object> convert2Map(Object object) {
        return JsonUtils.convert2Map(object);
    }

    @Override
    public String toString(Map<String, Object> jsonMap) {
        return JsonUtils.write2String(jsonMap);
    }
}
