package online.qiqiang.forest.rpc.common.serializer;

/**
 * @author qiqiang
 */
public class SerializerManager {
    private static final ForestSerializer serializer = new ObjectSerializer();

    public static ForestSerializer getSerializer() {
        return serializer;
    }
}
