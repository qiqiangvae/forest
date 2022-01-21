package online.qiqiang.forest.rpc.common.serializer;

/**
 * @author qiqiang
 */
public interface ForestSerializer {
    byte[] serialize(String body);

    byte[] serialize(Object object);

    String deserialize(byte[] body);

    <T> T deserialize(byte[] body, Class<T> clazz);
}
