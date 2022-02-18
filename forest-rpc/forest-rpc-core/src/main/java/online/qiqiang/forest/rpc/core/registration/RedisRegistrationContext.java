package online.qiqiang.forest.rpc.core.registration;

import lombok.RequiredArgsConstructor;
import online.qiqiang.forest.common.utils.JsonUtils;
import online.qiqiang.forest.common.utils.codec.Md5Utils;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.stream.Collectors;

/**
 * redis 注册中心
 *
 * @author qiqiang
 */
@RequiredArgsConstructor
public class RedisRegistrationContext implements RegistrationContext {

    private final Jedis jedis;
    private static final String providerPrefix = "provider";
    private static final String consumerPrefix = "consumer";

    @Override
    public void connect(ProviderInfo providerInfo) {
        String json = JsonUtils.write2String(providerInfo);
        jedis.hset(providerPrefix, providerInfo.getIp() + providerInfo.getPort(), json);
    }

    @Override
    public List<ProviderInfo> loadProvider() {
        return jedis.hgetAll(providerPrefix)
                .values().stream()
                .map(json -> JsonUtils.read2Object(json, ProviderInfo.class))
                .collect(Collectors.toList());
    }

    @Override
    public void connect(ConsumerInfo consumerInfo) {
        String json = JsonUtils.write2String(consumerInfo);
        jedis.hset(consumerPrefix, Md5Utils.md5(json), json);
    }

    @Override
    public List<ConsumerInfo> loadConsumer() {
        return jedis.hgetAll(consumerPrefix)
                .values().stream()
                .map(json -> JsonUtils.read2Object(json, ConsumerInfo.class))
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        jedis.del(providerPrefix);
        jedis.del(consumerPrefix);
    }
}
