package org.nature.forest.dubbo.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.filter.ContextFilter;
import org.nature.forest.framework.context.ForestContext;
import org.nature.forest.framework.context.ForestContextConstants;
import org.nature.forest.framework.trace.TraceConstants;

import java.util.Map;

/**
 * 上下文传递
 * 这里 order 设置为 -9999，是因为上下文的 attachment 必须要在 {@link ContextFilter} 才能设置进去，所以需要在设置在 -10000 之后
 *
 * @author qiqiang
 */
@Activate(group = CommonConstants.PROVIDER, order = -9999)
public class ForestContextProviderFilter implements Filter {
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
