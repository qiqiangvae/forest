package online.qiqiang.forest.rpc.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import online.qiqiang.forest.rpc.common.consts.MessageTypeEnum;
import online.qiqiang.forest.rpc.common.protocol.ForestRpcProtocol;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author qiqiang
 */
public class ForestRpcMessageDecode extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        ForestRpcProtocol forestRpcProtocol = new ForestRpcProtocol();
        int begin;
        // 在循环中找到协议开始的位置
        while (true) {
            // 本次协议包开始的位置
            begin = byteBuf.readerIndex();
            // 标记本次协议包开始的位置
            byteBuf.markReaderIndex();
            if (byteBuf.readableBytes() >= 4 && byteBuf.readInt() == ForestRpcProtocol.getStartFlag()) {
                break;
            }
            // 没有读到 HEAD_START，那么就读取下一个字节
            byteBuf.resetReaderIndex();
            byteBuf.readByte();
        }
        if (byteBuf.readableBytes() >= 41) {
            byte version = byteBuf.readByte();
            forestRpcProtocol.setVersion(version);
            long sessionId = byteBuf.readLong();
            forestRpcProtocol.setSessionId(sessionId);
            byte[] requestId = new byte[32];
            byteBuf.readBytes(requestId);
            forestRpcProtocol.setRequestId(new String(requestId, StandardCharsets.UTF_8));
        } else {
            byteBuf.readerIndex(begin);
            return;
        }
        if (byteBuf.readableBytes() >= 4) {
            int code = byteBuf.readInt();
            forestRpcProtocol.setMessageType(MessageTypeEnum.ofCode(code));
        } else {
            byteBuf.readerIndex(begin);
            return;
        }
        int length;
        if (byteBuf.readableBytes() >= 4 && byteBuf.readableBytes() >= (length = byteBuf.readInt())) {
            byte[] body = new byte[length];
            byteBuf.readBytes(body);
            forestRpcProtocol.setBody(body);
        } else {
            byteBuf.readerIndex(begin);
            return;
        }
        list.add(forestRpcProtocol);

    }
}
