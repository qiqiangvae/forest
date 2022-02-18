package online.qiqiang.forest.rpc.core.consumer.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.utils.JsonUtils;
import online.qiqiang.forest.rpc.core.registration.ConsumerInfo;
import online.qiqiang.forest.rpc.core.registration.LocalRegistrationContext;
import online.qiqiang.forest.rpc.core.registration.ProviderInfo;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;


/**
 * @author qiqiang
 */
@Slf4j
public class ForestRpcClient implements Closeable {
    private final ForestRpcConsumerProperties properties;
    private CompletableFuture<Void> future;
    private Bootstrap bootstrap;
    private ChannelFuture channelFuture;
    private CountDownLatch restartTimes = new CountDownLatch(3);
    private final LocalRegistrationContext registrationContext;
    @Getter
    private final ChannelWriter channelWriter;

    public ForestRpcClient(ForestRpcConsumerProperties properties) {
        this.properties = properties;
        this.channelWriter = new ChannelWriter();
        this.registrationContext = new LocalRegistrationContext();
    }

    /**
     * 启动
     */
    public void start() {
        register();
        future = CompletableFuture.runAsync(this::start0);
    }

    @Override
    public void close() {
        if (future != null) {
            future.cancel(true);
        }
        log.info("forest rpc client 关闭");
    }

    private void start0() {
        bootstrap = new Bootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        bootstrap.group(boss)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer());
        try {
            connect();
            channelFuture.channel().closeFuture().addListener((ChannelFutureListener) channelFuture -> connect()).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            boss.shutdownGracefully();
        }
    }

    private void connect() {
        for (ProviderInfo providerInfo : registrationContext.loadProvider()) {
            try {
                channelFuture = bootstrap.connect(new InetSocketAddress(providerInfo.getIp(), providerInfo.getPort()))
                        .addListener((ChannelFutureListener) channelFuture -> {
                            if (channelFuture.isSuccess()) {
                                restartTimes = new CountDownLatch(3);
                                channelWriter.setChannel(channelFuture.channel());
                            } else {
                                restartTimes.countDown();
                            }
                        }).sync();
            } catch (InterruptedException e) {
                log.error(e.getLocalizedMessage());
            }
        }
    }

    /**
     * 注册自己到注册中心
     */
    private void register() {
        ConsumerInfo consumerInfo = new ConsumerInfo();
        HttpUtil.post(properties.getRegisterUrl() + "/consumer", JsonUtils.write2String(consumerInfo));
        loadProvider();
    }

    private void loadProvider() {
        String json = HttpRequest.get(properties.getRegisterUrl() + "/provider").execute().body();
        this.registrationContext.clear();
        for (ProviderInfo info : JsonUtils.read2List(json, ProviderInfo.class)) {
            this.registrationContext.connect(info);
        }
    }
}
