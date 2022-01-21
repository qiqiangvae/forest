package online.qiqiang.forest.rpc.core.provider;

import io.netty.channel.Channel;
import online.qiqiang.forest.common.utils.BeanUtils;
import online.qiqiang.forest.rpc.core.client.ChannelWriter;
import online.qiqiang.forest.rpc.core.matedata.RpcWrapper;
import online.qiqiang.forest.rpc.core.matedata.ServiceMetaData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author qiqiang
 */
public class ServiceExecutor {
    private static final ChannelWriter channelWriter = new ChannelWriter();

    public static void execute(Channel channel, String requestId, RpcWrapper rpcWrapper) {
        ServiceMetaData metaData = rpcWrapper.getMetaData();
        Object service = ServiceRegister.getService(metaData);
        try {
            Method method = service.getClass().getDeclaredMethod(metaData.getMethodName(), metaData.getParamClasses());
            Object result = method.invoke(service, rpcWrapper.getParams());
            channelWriter.setChannel(channel);
            RpcWrapper responseWrapper = new RpcWrapper();
            BeanUtils.copy(rpcWrapper, responseWrapper);
            responseWrapper.setResponse(result);
            channelWriter.writeAndFlush(requestId, responseWrapper);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
