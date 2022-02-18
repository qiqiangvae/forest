package online.qiqiang.forest.rpc.spring.consumer;

import lombok.RequiredArgsConstructor;
import online.qiqiang.forest.common.utils.id.IdGenerator;
import online.qiqiang.forest.rpc.common.consts.MessageTypeEnum;
import online.qiqiang.forest.rpc.core.annotation.ForestReference;
import online.qiqiang.forest.rpc.core.consumer.InvokeExecutor;
import online.qiqiang.forest.rpc.core.consumer.client.ChannelWriter;
import online.qiqiang.forest.rpc.core.matedata.RpcWrapper;
import online.qiqiang.forest.rpc.core.matedata.ServiceMetaData;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author qiqiang
 */
@RequiredArgsConstructor
public class ForestReferenceProxyFactory {

    private final ChannelWriter channelWriter;

    public Object newProxyReference(ForestReference forestReference, Class<?> fieldClass, int timeout) {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{fieldClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Object.class.equals(method.getDeclaringClass())) {
                    return method.invoke(this, args);
                } else {
                    RpcWrapper rpcWrapper = new RpcWrapper();
                    ServiceMetaData metaData = new ServiceMetaData();
                    metaData.setClazz(fieldClass);
                    metaData.setMethodName(method.getName());
                    metaData.setVersion(forestReference.version());
                    metaData.setParamClasses(method.getParameterTypes());
                    metaData.setReturnClazz(String.class);
                    rpcWrapper.setMetaData(metaData);
                    rpcWrapper.setParams(args);
                    String requestId = IdGenerator.uuid();
                    channelWriter.sendMessage(requestId, rpcWrapper, MessageTypeEnum.REQ);
                    return InvokeExecutor.getResponse(requestId, timeout);
                }
            }
        });
    }
}
