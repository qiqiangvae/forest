package org.nature.forest.mvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.nature.forest.common.utils.IdGenerator;
import org.nature.forest.framework.context.ForestContext;
import org.nature.forest.framework.trace.TraceConstants;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author : Zhang Huang
 * @date : 2021-09-13 9:52 上午
 */
@Slf4j
public class TraceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            ForestContext.set(TraceConstants.TRACE_ID, IdGenerator.uuid());
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
