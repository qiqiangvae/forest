package online.qiqiang.forest.rpc.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;


/**
 * @author qiqiang
 */
@Slf4j
public class ForestRpcClient implements Closeable {
    private final ForestRpcClientProperties properties;
    private CompletableFuture<Void> future;
    private Bootstrap bootstrap;
    private ChannelFuture channelFuture;
    private CountDownLatch restartTimes = new CountDownLatch(3);
    @Getter
    private final ChannelWriter channelWriter = new ChannelWriter();

    public ForestRpcClient(ForestRpcClientProperties properties) {
        this.properties = properties;
    }

    /**
     * 启动
     */
    public void start() {
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

    private void connect() throws InterruptedException {
        channelFuture = bootstrap.connect(new InetSocketAddress(properties.getHost(), properties.getPort()))
                .addListener((ChannelFutureListener) channelFuture -> {
                    if (channelFuture.isSuccess()) {
                        restartTimes = new CountDownLatch(3);
                        channelWriter.setChannel(channelFuture.channel());
                    } else {
                        restartTimes.countDown();
                    }
                }).sync();
    }
}
