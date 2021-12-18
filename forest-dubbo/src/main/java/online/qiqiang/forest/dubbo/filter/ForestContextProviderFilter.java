package online.qiqiang.forest.dubbo.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.filter.ContextFilter;
import online.qiqiang.forest.framework.context.ForestContext;
import online.qiqiang.forest.framework.context.ForestContextConstants;
import online.qiqiang.forest.framework.trace.TraceConstants;

import java.util.Map;

/**
 * 上下文传递
 * 这里 order 设置为 -9999，是因为上下文的 attachment 必须要在 {@link ContextFilter} 才能设置进去，所以需要在设置在 -10000 之后
 *
 * @author qiqiang
 */
@Activate(group = CommonConstants.PROVIDER, order = ForestContextProviderFilter.ORDER)
@SuppressWarnings("unused")
public class ForestContextProviderFilter implements Filter {
    final static int ORDER = -9999;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Map<String, Object> attachments = RpcContext.getServerContext().getObjectAttachments();
        String sequence = (String) attachments.get(ForestContextConstants.SEQUENCE);
        String traceId = (String) attachments.get(TraceConstants.TRACE_ID);
        ForestContext.set(ForestContextConstants.SEQUENCE, sequence);
        ForestContext.set(TraceConstants.TRACE_ID, traceId);
        return invoker.invoke(invocation);
    }
}
