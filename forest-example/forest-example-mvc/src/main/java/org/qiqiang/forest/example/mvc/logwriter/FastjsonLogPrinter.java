package org.qiqiang.forest.example.mvc.logwriter;

import com.alibaba.fastjson.JSON;
import org.qiqiang.forest.framework.log.LogPrinterFunction;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author qiqiang
 */
@Component
@SuppressWarnings("unchecked")
public class FastjsonLogPrinter implements LogPrinterFunction {

    @Override
    public Map<String, Object> convert2Map(Object object) {
        return JSON.parseObject(JSON.toJSONString(object), Map.class);
    }

    @Override
    public String toString(Map<String, Object> jsonMap) {
        return JSON.toJSONString(jsonMap);
    }
}
