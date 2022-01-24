package online.qiqiang.forest.rpc.core.consumer.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import online.qiqiang.forest.rpc.common.protocol.ForestRpcProtocol;
import online.qiqiang.forest.rpc.common.serializer.SerializerManager;
import online.qiqiang.forest.rpc.core.consumer.InvokeExecutor;
import online.qiqiang.forest.rpc.core.matedata.RpcWrapper;

/**
 * @author qiqiang
 */
@ChannelHandler.Sharable
public class ClientMessageHandler extends SimpleChannelInboundHandler<ForestRpcProtocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ForestRpcProtocol protocol) {
        RpcWrapper responseWrapper = SerializerManager.getSerializer().deserialize(protocol.getBody(), RpcWrapper.class);
        InvokeExecutor.setResponse(protocol.getRequestId(), responseWrapper);
    }
}
