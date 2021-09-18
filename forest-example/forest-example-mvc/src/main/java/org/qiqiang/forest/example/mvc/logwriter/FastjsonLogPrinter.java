package org.nature.forest.example.mvc.logwriter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.nature.forest.framework.log.LogPrinterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author qiqiang
 */
@Component
@SuppressWarnings("unchecked")
public class FastjsonLogPrinter implements LogPrinterFunction {

    @Override
    public Map<String, Object> convert2Map(Object object) {
        return JSONObject.parseObject(JSONObject.toJSONString(object, new SerializeConfig() {
            @Override
            public ObjectSerializer getObjectWriter(Class<?> clazz) {
                if (MultipartFile.class.isAssignableFrom(clazz)) {
                    return new MultipartFileObjectSerializer(MultipartFile.class);
                }
                return super.getObjectWriter(clazz);
            }
        }), Map.class);
    }

    @Override
    public String toString(Map<String, Object> jsonMap) {
        return JSONObject.toJSONString(jsonMap);
    }


    static class MultipartFileObjectSerializer extends JavaBeanSerializer {

        public MultipartFileObjectSerializer(Class<?> beanType) {
            super(beanType);
        }

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
                          int features) {
            if (object instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) object;
                serializer.write(String.format("文件名【%s】，大小【%d】", file.getOriginalFilename(), file.getSize()));
            }
        }
    }
}
