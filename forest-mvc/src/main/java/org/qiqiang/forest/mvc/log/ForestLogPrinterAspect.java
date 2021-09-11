package org.qiqiang.forest.mvc.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.qiqiang.forest.common.utils.JsonUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiqiang
 */
@EnableAspectJAutoProxy
@Aspect
@Slf4j
public class ForestLogPrinterAspect {

    @Setter
    private ObjectMapper objectMapper;
    private String ignoreText = "@该字段内容已忽略@";
    private final Set<String> globalIgnoreReq = new HashSet<>();
    private final Set<String> globalIgnoreResp = new HashSet<>();


    @Pointcut("@annotation(org.qiqiang.forest.mvc.log.LogPrinter)||@within(org.qiqiang.forest.mvc.log.LogPrinter)")
    public void annotationPointcut() {

    }

    public void setIgnoreText(String ignoreText) {
        if (StringUtils.isNotBlank(ignoreText)) {
            this.ignoreText = ignoreText;
        }
    }

    public void addGlobalIgnoreReq(Collection<String> ignores) {
        globalIgnoreReq.addAll(ignores);
        log.info("配置全局忽略请求字段[{}]", String.join(",", ignores));
    }

    public void addGlobalIgnoreResp(Collection<String> ignores) {
        globalIgnoreResp.addAll(ignores);
        log.info("配置全局忽略返回字段[{}]", String.join(",", ignores));
    }

    @PostConstruct
    public void postConstruct() {
        JsonUtils.init(objectMapper);
    }

    @Around("annotationPointcut()")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        // 处理类上的配置
        Object target = pjp.getTarget();
        LogPrinter classLogPointer = target.getClass().getAnnotation(LogPrinter.class);
        Set<String> ignoreReq = new HashSet<>(globalIgnoreReq);
        Set<String> ignoreResp = new HashSet<>(globalIgnoreResp);
        if (classLogPointer != null) {
            ignoreReq.addAll(Stream.of(classLogPointer.ignoreReq()).collect(Collectors.toSet()));
            ignoreResp.addAll(Stream.of(classLogPointer.ignoreResp()).collect(Collectors.toSet()));
        }
        // 处理方法上的配置
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LogPrinter logPointer = method.getAnnotation(LogPrinter.class);
        if (logPointer != null) {
            ignoreReq.addAll(Stream.of(logPointer.ignoreReq()).collect(Collectors.toSet()));
            ignoreResp.addAll(Stream.of(logPointer.ignoreResp()).collect(Collectors.toSet()));
        }
        // 入参打印
        String[] parameterNames = signature.getParameterNames();
        Object[] args = pjp.getArgs();
        Map<String, Object> requestLog = getRequestLog(ignoreReq, parameterNames, args);
        log.info("[{}]入参：[{}]", method.getName(), JsonUtils.write2String(requestLog));
        // 真正处理
        Object result = pjp.proceed();
        // 出参打印
        String responseLog = getResponseLog(ignoreResp, result);
        log.info("[{}]出参：[{}]", method.getName(), JsonUtils.write2String(responseLog));
        return result;
    }

    private String getResponseLog(Set<String> ignoreResp, Object result) {
        if (isSimpleType(result)) {
            return result.toString();
        }
        Map<String, Object> map = JsonUtils.convert2Map(result);
        for (String ignore : ignoreResp) {
            removeIgnoreArgs(map, StringUtils.split(ignore, ".", 2));
        }
        return JsonUtils.write2String(map);
    }

    private boolean isSimpleType(Object result) {
        Class<?> clazz = result.getClass();
        // 是否字符
        return CharSequence.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)
                // 是否布尔
                || Boolean.class.isAssignableFrom(clazz)
                // 是否数字
                || Number.class.isAssignableFrom(clazz)
                // 是否时间
                || Date.class.isAssignableFrom(clazz)
                || Temporal.class.isAssignableFrom(clazz);
    }

    private Map<String, Object> getRequestLog(Set<String> ignoreReq, String[] parameterNames, Object[] args) {
        Map<String, Object> argsMap = new HashMap<>(args.length);
        for (int i = 0; i < parameterNames.length; i++) {
            argsMap.put(parameterNames[i], args[i]);
        }
        Map<String, Object> map = JsonUtils.convert2Map(argsMap);
        for (String ignore : ignoreReq) {
            removeIgnoreArgs(map, StringUtils.split(ignore, ".", 2));
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private void removeIgnoreArgs(Map<String, Object> map, String[] ignore) {
        if (ignore == null || ignore.length == 0) {
            return;
        }
        if (ignore.length == 1) {
            map.put(ignore[0], ignoreText);
        } else {
            String[] childrenIgnore = new String[ignore.length - 1];
            System.arraycopy(ignore, 1, childrenIgnore, 0, ignore.length - 1);
            removeIgnoreArgs((Map<String, Object>) map.get(ignore[0]), childrenIgnore);
        }
    }
}
