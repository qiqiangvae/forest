package online.qiqiang.forest.rpc.core.provider;

import io.netty.channel.Channel;
import online.qiqiang.forest.common.utils.BeanUtils;
import online.qiqiang.forest.rpc.core.consumer.client.ChannelWriter;
import online.qiqiang.forest.rpc.core.exception.RpcInvokeException;
import online.qiqiang.forest.rpc.core.matedata.RpcWrapper;
import online.qiqiang.forest.rpc.core.matedata.ServiceMetaData;

import java.lang.reflect.Method;


/**
 * @author qiqiang
 */
public class ServiceExecutor {
    private static final ChannelWriter channelWriter = new ChannelWriter();

    public static void execute(Channel channel, String requestId, RpcWrapper rpcWrapper) {
        RpcWrapper responseWrapper = new RpcWrapper();
        BeanUtils.copy(rpcWrapper, responseWrapper);
        channelWriter.setChannel(channel);
        try {
            ServiceMetaData metaData = rpcWrapper.getMetaData();
            Object service = ServiceRegister.getService(metaData);
            if (service == null) {
                throw new RpcInvokeException("服务不存在");
            }
            Method method = service.getClass().getDeclaredMethod(metaData.getMethodName(), metaData.getParamClasses());
            Object result = method.invoke(service, rpcWrapper.getParams());
            responseWrapper.setResponse(result);
        } catch (Exception e) {
            e.printStackTrace();
            responseWrapper.setException(new RpcInvokeException(e));
        } finally {
            channelWriter.writeAndFlush(requestId, responseWrapper);
        }
    }
}
