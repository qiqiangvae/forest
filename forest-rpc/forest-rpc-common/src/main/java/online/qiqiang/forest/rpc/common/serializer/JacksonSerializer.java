package online.qiqiang.forest.rpc.common.serializer;

import online.qiqiang.forest.common.utils.JsonUtils;

import java.nio.charset.StandardCharsets;

/**
 * jackson 序列化
 *
 * @author qiqiang
 */
public class JacksonSerializer implements ForestSerializer {

    @Override
    public byte[] serialize(String body) {
        return body.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(Object object) {
        return serialize(JsonUtils.write2String(object));
    }

    @Override
    public String deserialize(byte[] body) {
        return new String(body);
    }

    @Override
    public <T> T deserialize(byte[] body, Class<T> clazz) {
        return JsonUtils.read2Object(deserialize(body), clazz);
    }
}
