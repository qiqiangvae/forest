package online.qiqiang.forest.rpc.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * @author qiqiang
 */
@Slf4j
public class ForestRpcServer implements Closeable {
    private final ForestRpcServerProperties properties;
    private CompletableFuture<Void> future;
    private ServerBootstrap serverBootstrap;
    private ChannelFuture channelFuture;
    private CountDownLatch restartTimes = new CountDownLatch(3);

    public ForestRpcServer(ForestRpcServerProperties properties) {
        this.properties = properties;
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
        channelFuture = serverBootstrap.bind(new InetSocketAddress(properties.getPort()))
                .addListener((ChannelFutureListener) channelFuture -> {
                    if (channelFuture.isSuccess()) {
                        restartTimes = new CountDownLatch(3);
                        log.info("启动成功，端口[{}]", properties.getPort());
                    } else {
                        restartTimes.countDown();
                    }
                }).sync();
    }
}
