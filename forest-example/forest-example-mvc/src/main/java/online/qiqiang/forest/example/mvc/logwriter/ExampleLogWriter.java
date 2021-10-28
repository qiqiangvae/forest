package online.qiqiang.forest.example.mvc.logwriter;

import online.qiqiang.forest.framework.log.LogResponseWriter;

import java.util.Map;

/**
 * @author qiqiang
 */
public class ExampleLogWriter implements LogResponseWriter {

    @Override
    public Map<String, Object> write(Object result, Map<String, Object> jsonMap) {
        jsonMap.put("specialKey", "我是一个被新增的字段");
        return jsonMap;
    }
}
