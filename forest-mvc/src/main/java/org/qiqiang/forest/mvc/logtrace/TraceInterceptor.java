package org.qiqiang.forest.mvc.logtrace;

import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : Zhang Huang
 * @date : 2021-09-10 2:56 下午
 */
public class TraceInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.clear();
        ThreadMdcUtil.setTraceIdIfAbsent();
        return true;
    }
}
