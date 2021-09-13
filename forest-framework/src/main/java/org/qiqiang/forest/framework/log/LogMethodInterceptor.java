package org.qiqiang.forest.framework.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.qiqiang.forest.common.utils.JsonUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiqiang
 */
@Slf4j
public class LogMethodInterceptor implements MethodInterceptor {

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();


    @Setter
    private ObjectMapper objectMapper;
    private String ignoreText = "@该字段内容已忽略@";
    private final Set<String> globalIgnoreReq = new HashSet<>();
    private final Set<String> globalIgnoreResp = new HashSet<>();

    public void setIgnoreText(String ignoreText) {
        if (StringUtils.isNotBlank(ignoreText)) {
            this.ignoreText = ignoreText;
        }
    }

    public void addGlobalIgnoreReq(Collection<String> ignores) {
        if (!CollectionUtils.isEmpty(ignores)) {
            globalIgnoreReq.addAll(ignores);
            log.info("配置全局忽略请求字段[{}]", String.join(",", ignores));
        }
    }

    public void addGlobalIgnoreResp(Collection<String> ignores) {
        if (!CollectionUtils.isEmpty(ignores)) {
            globalIgnoreResp.addAll(ignores);
            log.info("配置全局忽略返回字段[{}]", String.join(",", ignores));
        }
    }

    @PostConstruct
    public void postConstruct() {
        JsonUtils.init(objectMapper);
    }

    private String getResponseLog(Set<String> ignoreResp, Object result) {
        if (result == null) {
            return null;
        }
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

    private Map<String, Object> getRequestLog(Set<String> ignoreReq, String[] parameters, Object[] args) {
        Map<String, Object> argsMap = new HashMap<>(args.length);
        if (parameters == null) {
            return new HashMap<>(0);
        }
        for (int i = 0; i < parameters.length; i++) {
            argsMap.put(parameters[i], args[i]);
        }
        Map<String, Object> map = JsonUtils.convert2Map(argsMap);
        for (String ignore : ignoreReq) {
            removeIgnoreArgs(map, StringUtils.split(ignore, ".", 2));
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private void removeIgnoreArgs(Map<String, Object> map, String[] ignore) {
        if (ignore == null || ignore.length == 0 || !map.containsKey(ignore[0])) {
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


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        Object target = methodInvocation.getThis();
        Object[] args = methodInvocation.getArguments();
        // 处理类上的配置
        assert target != null;
        LogPrinter classLogPointer = target.getClass().getAnnotation(LogPrinter.class);
        boolean enable = true;
        Set<String> ignoreReq = new HashSet<>(globalIgnoreReq);
        Set<String> ignoreResp = new HashSet<>(globalIgnoreResp);
        if (classLogPointer != null) {
            enable = classLogPointer.enable();
            ignoreReq.addAll(Stream.of(classLogPointer.ignoreReq()).collect(Collectors.toSet()));
            ignoreResp.addAll(Stream.of(classLogPointer.ignoreResp()).collect(Collectors.toSet()));
        }
        // 处理方法上的配置
        LogPrinter logPointer = method.getAnnotation(LogPrinter.class);
        if (enable && logPointer != null) {
            enable = logPointer.enable();
            ignoreReq.addAll(Stream.of(logPointer.ignoreReq()).collect(Collectors.toSet()));
            ignoreResp.addAll(Stream.of(logPointer.ignoreResp()).collect(Collectors.toSet()));
        }
        String name = target.getClass().getSimpleName() + "#" + method.getName();
        if (enable) {
            try {
                // 入参打印
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                Map<String, Object> requestLog = getRequestLog(ignoreReq, parameterNames, args);
                log.info("[{}]入参：[{}]", name, JsonUtils.write2String(requestLog));
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        // 真正处理
        Object result = methodInvocation.proceed();
        if (enable) {
            try {
                // 出参打印
                String responseLog = getResponseLog(ignoreResp, result);
                log.info("[{}]出参：[{}]", name, responseLog);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }

        }
        return result;
    }
}
