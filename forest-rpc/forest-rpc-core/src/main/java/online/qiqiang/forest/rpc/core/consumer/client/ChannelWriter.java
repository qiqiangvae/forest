package online.qiqiang.forest.rpc.core.consumer.client;

import io.netty.channel.Channel;
import online.qiqiang.forest.rpc.common.consts.MessageTypeEnum;
import online.qiqiang.forest.rpc.common.protocol.ForestRpcProtocol;

/**
 * @author qiqiang
 */
public class ChannelWriter {
    private Channel defaultChannel;

    public void sendMessage(String requestId, Object message, MessageTypeEnum messageType) {
        sendMessage(defaultChannel, requestId, message, messageType);
    }

    public void sendMessage(Channel channel, String requestId, Object message, MessageTypeEnum messageType) {
        ForestRpcProtocol forestRpcProtocol = new ForestRpcProtocol();
        forestRpcProtocol.setVersion((byte) 1);
        forestRpcProtocol.setSessionId(1L);
        forestRpcProtocol.setRequestId(requestId);
        forestRpcProtocol.setMessageType(messageType);
        forestRpcProtocol.setBody(message);
        channel.writeAndFlush(forestRpcProtocol);
    }

    public void setChannel(Channel defaultChannel) {
        this.defaultChannel = defaultChannel;
    }
}
