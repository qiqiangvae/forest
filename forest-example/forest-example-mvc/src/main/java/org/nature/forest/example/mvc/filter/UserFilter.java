package org.nature.forest.example.mvc.filter;

import org.nature.forest.framework.context.ForestContext;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author qiqiang
 */
@Component
public class UserFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            ForestContext.setRemote("user", "admin");
            filterChain.doFilter(servletRequest,servletResponse);
        }finally {
            ForestContext.clear();
        }

    }
}
