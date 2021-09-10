package org.qiqiang.forest.mvc.log;

import org.slf4j.MDC;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : Zhang Huang
 * @date : 2021-09-10 2:56 下午
 */
public class TraceInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.clear();
        ThreadMdcUtil.setTraceIdIfAbsent();
        return true;
    }
}
