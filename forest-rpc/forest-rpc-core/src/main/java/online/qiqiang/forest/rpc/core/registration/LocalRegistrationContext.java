package online.qiqiang.forest.rpc.core.registration;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地注册中心
 *
 * @author qiqiang
 */
public class LocalRegistrationContext implements RegistrationContext {
    /**
     * 服务提供者
     */
    private static final List<ProviderInfo> providerList = new ArrayList<>();
    /**
     * 服务消费者
     */
    private static final List<ConsumerInfo> consumerList = new ArrayList<>();

    @Override
    public void connect(ProviderInfo providerInfo) {
        providerList.add(providerInfo);
    }

    @Override
    public List<ProviderInfo> loadProvider() {
        return providerList;
    }

    @Override
    public void connect(ConsumerInfo consumerInfo) {
        consumerList.add(consumerInfo);
    }

    @Override
    public List<ConsumerInfo> loadConsumer() {
        return consumerList;
    }

    @Override
    public void clear() {
        providerList.clear();
        consumerList.clear();
    }
}
