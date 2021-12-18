package online.qiqiang.forest.framework.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.utils.JsonUtils;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;
import java.util.Map;

/**
 * 用项目自带的 JsonUtils 实现
 *
 * @author qiqiang
 */
@Slf4j
public class DefaultLogPrinter implements LogPrinterFunction {

    public DefaultLogPrinter(ObjectProvider<List<JacksonLogPrinterCustomizer>> jacksonLogPrinterCustomizerProvider) {
        // 加载自定义模块
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        jacksonLogPrinterCustomizerProvider.ifAvailable(list -> list.forEach(customizer -> customizer.getModules().forEach(objectMapper::registerModule)));
    }

    @Override
    public Map<String, Object> convert2Map(Object object) {
        return JsonUtils.convert2Map(object);
    }

    @Override
    public String toString(Map<String, Object> jsonMap) {
        return JsonUtils.write2String(jsonMap);
    }
}
