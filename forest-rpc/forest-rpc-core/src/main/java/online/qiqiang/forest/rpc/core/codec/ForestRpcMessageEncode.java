package online.qiqiang.forest.rpc.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import online.qiqiang.forest.rpc.common.protocol.ForestRpcProtocol;

import java.nio.charset.StandardCharsets;

/**
 * @author qiqiang
 */
public class ForestRpcMessageEncode extends MessageToByteEncoder<ForestRpcProtocol> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ForestRpcProtocol forestRpcMessage, ByteBuf byteBuf) {
        byteBuf.writeInt(ForestRpcProtocol.getStartFlag());
        byteBuf.writeByte(forestRpcMessage.getVersion());
        byteBuf.writeLong(forestRpcMessage.getSessionId());
        byteBuf.writeBytes(forestRpcMessage.getRequestId().getBytes(StandardCharsets.UTF_8));
        byteBuf.writeInt(forestRpcMessage.getMessageType().getCode());
        byte[] bodyBytes = forestRpcMessage.getBody();
        byteBuf.writeInt(bodyBytes.length);
        byteBuf.writeBytes(bodyBytes);
    }
}
