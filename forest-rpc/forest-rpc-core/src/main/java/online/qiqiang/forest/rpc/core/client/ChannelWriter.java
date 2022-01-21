package online.qiqiang.forest.rpc.core.client;

import io.netty.channel.Channel;
import online.qiqiang.forest.rpc.common.consts.MessageTypeEnum;
import online.qiqiang.forest.rpc.common.protocol.ForestRpcProtocol;

/**
 * @author qiqiang
 */
public class ChannelWriter {
    private Channel defaultChannel;

    public void writeAndFlush(String requestId, Object message) {
        writeAndFlush(defaultChannel, requestId, message);
    }

    public void writeAndFlush(Channel channel, String requestId, Object message) {
        ForestRpcProtocol forestRpcMessage = new ForestRpcProtocol();
        forestRpcMessage.setVersion((byte) 1);
        forestRpcMessage.setSessionId(1L);
        forestRpcMessage.setRequestId(requestId);
        forestRpcMessage.setMessageType(MessageTypeEnum.REQ);
        forestRpcMessage.setBody(message);
        channel.writeAndFlush(forestRpcMessage);
    }

    public void setChannel(Channel defaultChannel) {
        this.defaultChannel = defaultChannel;
    }
}
