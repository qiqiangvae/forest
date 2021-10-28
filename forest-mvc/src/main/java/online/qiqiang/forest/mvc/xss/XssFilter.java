package online.qiqiang.forest.mvc.xss;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 防止 XSS 攻击过滤器
 *
 * @author qiqiang
 */
@Slf4j
public class XssFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("(XssFilter) initialize");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest rq = (HttpServletRequest) request;
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(rq);
        chain.doFilter(xssRequest, response);
    }

    @Override
    public void destroy() {
        log.debug("(XssFilter) destroy");
    }

}
