package online.qiqiang.forest.dubbo.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import online.qiqiang.forest.framework.context.ForestContext;
import online.qiqiang.forest.framework.context.ForestContextConstants;
import online.qiqiang.forest.framework.trace.TraceConstants;

import java.util.Map;

/**
 * 上下文传递
 * 将 TRACE_ID 传过去，可以实现链路跟踪
 * 将 SEQUENCE 传过去，可以在 provider 中获取到远程上下文
 *
 * @author qiqiang
 */
@Activate(group = CommonConstants.CONSUMER)
public class ForestContextConsumerFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String sequence = ForestContext.getSequence();
        Map<String, Object> attachments = RpcContext.getServerContext().getObjectAttachments();
        attachments.put(ForestContextConstants.SEQUENCE, sequence);
        attachments.put(TraceConstants.TRACE_ID, ForestContext.get(TraceConstants.TRACE_ID));
        return invoker.invoke(invocation);
    }
}
