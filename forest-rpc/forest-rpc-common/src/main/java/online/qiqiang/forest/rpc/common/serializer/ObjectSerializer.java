package online.qiqiang.forest.rpc.common.serializer;

import java.io.*;

/**
 * @author qiqiang
 */
public class ObjectSerializer implements ForestSerializer {
    @Override
    public byte[] serialize(String body) {
        return serialize(body);
    }

    @Override
    public byte[] serialize(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String deserialize(byte[] body) {
        return deserialize(body, String.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] body, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
