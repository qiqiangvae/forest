package online.qiqiang.forest.rpc.core.registration;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册中心内容
 *
 * @author qiqiang
 */
public class RegistrationContext {
    /**
     * 服务提供者
     */
    private static final List<ProviderInfo> providerList = new ArrayList<>();
    /**
     * 服务消费者
     */
    private static final List<ConsumerInfo> consumerList = new ArrayList<>();

    public static void connect(ProviderInfo providerInfo) {
        providerList.add(providerInfo);
    }

    public static void connect(ConsumerInfo consumerInfo) {
        consumerList.add(consumerInfo);
    }
}
