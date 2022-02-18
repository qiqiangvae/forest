package online.qiqiang.forest.rpc.core.registration;

import java.util.List;

/**
 * @author qiqiang
 */
public interface RegistrationContext {
    void connect(ProviderInfo providerInfo);

    List<ProviderInfo> loadProvider();

    void connect(ConsumerInfo consumerInfo);

    List<ConsumerInfo> loadConsumer();

    void clear();

}
