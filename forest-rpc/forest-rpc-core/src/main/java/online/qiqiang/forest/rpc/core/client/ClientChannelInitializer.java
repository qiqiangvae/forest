package online.qiqiang.forest.rpc.core.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import online.qiqiang.forest.rpc.core.codec.ForestRpcMessageDecode;
import online.qiqiang.forest.rpc.core.codec.ForestRpcMessageEncode;

/**
 * @author qiqiang
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final ClientMessageHandler clientMessageHandler = new ClientMessageHandler();


    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new ForestRpcMessageDecode());
        pipeline.addLast(new ForestRpcMessageEncode());
        pipeline.addLast(clientMessageHandler);
    }
}
