package org.qiqiang.forest.mvc.trace;

import org.qiqiang.forest.common.utils.IdGenerator;
import org.slf4j.MDC;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.qiqiang.forest.mvc.ForestMvcConstants.TRACE_ID;


/**
 * @author : Zhang Huang
 * @date : 2021-09-10 2:58 下午
 */
public class ThreadMdcUtil {

    public static String createTraceId() {
        String uuid = IdGenerator.uuid();
        return DigestUtils.md5DigestAsHex(uuid.getBytes(StandardCharsets.UTF_8)).substring(8, 24);
    }

    public static void setTraceIdIfAbsent() {
        if (MDC.get(TRACE_ID) == null) {
            MDC.put(TRACE_ID, createTraceId());
        }
    }

    @SuppressWarnings("unused")
    public static void setTraceId() {
        MDC.put(TRACE_ID, createTraceId());
    }

    @SuppressWarnings("unused")
    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
