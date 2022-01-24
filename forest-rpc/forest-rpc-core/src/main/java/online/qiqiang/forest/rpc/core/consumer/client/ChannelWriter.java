package online.qiqiang.forest.rpc.core.consumer.client;

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
        ForestRpcProtocol forestRpcProtocol = new ForestRpcProtocol();
        forestRpcProtocol.setVersion((byte) 1);
        forestRpcProtocol.setSessionId(1L);
        forestRpcProtocol.setRequestId(requestId);
        forestRpcProtocol.setMessageType(MessageTypeEnum.REQ);
        forestRpcProtocol.setBody(message);
        channel.writeAndFlush(forestRpcProtocol);
    }

    public void setChannel(Channel defaultChannel) {
        this.defaultChannel = defaultChannel;
    }
}
