package online.qiqiang.forest.mvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import online.qiqiang.forest.common.utils.id.IdGenerator;
import online.qiqiang.forest.framework.context.ForestContext;
import online.qiqiang.forest.framework.trace.TraceConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Zhang Huang
 */
@Slf4j
public class TraceFilter implements Filter {
    private static final String TRACE_ID_HEADER = "TRACE-ID";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String traceId = httpServletRequest.getHeader(TRACE_ID_HEADER);
            if (StringUtils.isBlank(traceId)) {
                traceId = IdGenerator.uuid();
            }
            ForestContext.set(TraceConstants.TRACE_ID, traceId);
            chain.doFilter(request, response);
        } finally {
            ForestContext.clear();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
