package online.qiqiang.forest.rpc.core.provider.server;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.utils.JsonUtils;
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
public class ForestRpcServer implements Closeable {
    private final ForestRpcProviderProperties properties;
    private CompletableFuture<Void> future;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;
    private CountDownLatch restartTimes = new CountDownLatch(3);
    private final LocalRegistrationContext registrationContext;

    public ForestRpcServer(ForestRpcProviderProperties properties) {
        this.properties = properties;
        this.registrationContext = new LocalRegistrationContext();
    }

    /**
     * 启动 forest rpc server
     */
    public void start() {
        future = CompletableFuture.runAsync(this::start0);
    }

    @Override
    public void close() {
        if (future != null) {
            future.cancel(true);
        }
        log.info("forest rpc server 关闭");
    }

    private void start0() {
        serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(properties.getWorkers());
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerChannelInitializer());
        try {
            bind();
            channelFuture.channel().closeFuture().addListener((ChannelFutureListener) channelFuture -> bind()).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private void bind() throws InterruptedException {
        if (restartTimes.getCount() == 0) {
            return;
        }
        channelFuture = serverBootstrap.bind(new InetSocketAddress(properties.getExposePort()))
                .addListener((ChannelFutureListener) channelFuture -> {
                    if (channelFuture.isSuccess()) {
                        restartTimes = new CountDownLatch(3);
                        log.info("启动成功，端口[{}]", properties.getExposePort());
                        register();
                    } else {
                        restartTimes.countDown();
                    }
                }).sync();
    }

    /**
     * 注册自己到注册中心
     */
    private void register() {
        ProviderInfo providerInfo = new ProviderInfo();
        HttpUtil.post(properties.getRegisterUrl() + "/provider", JsonUtils.write2String(providerInfo));
        String json = HttpRequest.get(properties.getRegisterUrl()).execute().body();
        this.registrationContext.clear();
        for (ProviderInfo info : JsonUtils.read2List(json, ProviderInfo.class)) {
            this.registrationContext.connect(info);
        }
    }

}
