package online.qiqiang.forest.rpc.core.provider.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import online.qiqiang.forest.rpc.common.protocol.ForestRpcProtocol;
import online.qiqiang.forest.rpc.core.matedata.RpcWrapper;
import online.qiqiang.forest.rpc.core.provider.ServiceExecutor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author qiqiang
 */
@ChannelHandler.Sharable
public class ServerMessageHandler extends SimpleChannelInboundHandler<ForestRpcProtocol> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ForestRpcProtocol protocol) {
        // todo 校验 session
        byte[] body = protocol.getBody();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(body))) {
            RpcWrapper rpcWrapper = (RpcWrapper) objectInputStream.readObject();
            ServiceExecutor.execute(channelHandlerContext.channel(), protocol.getRequestId(), rpcWrapper);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
