package online.qiqiang.forest.rpc.core.provider.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import online.qiqiang.forest.rpc.core.codec.ForestRpcMessageDecode;
import online.qiqiang.forest.rpc.core.codec.ForestRpcMessageEncode;

/**
 * @author qiqiang
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final ServerMessageHandler serverMessageHandler = new ServerMessageHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new ForestRpcMessageDecode());
        pipeline.addLast(new ForestRpcMessageEncode());
        pipeline.addLast(serverMessageHandler);
    }
}
