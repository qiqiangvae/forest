package online.qiqiang.forest.security.intercept;

import online.qiqiang.forest.common.utils.codec.Base64Utils;
import online.qiqiang.forest.security.exeception.SecurityException;
import online.qiqiang.forest.security.annotation.Basic;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qiqiang
 */
@Aspect
public class BasicInterceptor {
    private final static String BASIC_REALM = "basic realm=\"no auth\"";


    @Pointcut("@annotation(online.qiqiang.forest.security.annotation.Basic)")
    public void basicPointcut() {

    }

    @Before("basicPointcut()")
    public void before(JoinPoint joinPoint) throws IOException {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        HttpServletResponse response = sra.getResponse();
        String base6AuthStr = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (base6AuthStr == null) {
            if (response != null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.addHeader(HttpHeaders.WWW_AUTHENTICATE, BASIC_REALM);
                response.flushBuffer();
            }
            throw new SecurityException("basic 认证失败.");
        }
        // 获取配置
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Basic basic = signature.getMethod().getAnnotation(Basic.class);
        String encode = Base64Utils.encode(basic.username() + ":" + basic.password());
        if (!StringUtils.equalsIgnoreCase(encode, base6AuthStr.substring(6))) {
            if (response != null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.addHeader(HttpHeaders.WWW_AUTHENTICATE, BASIC_REALM);
                response.flushBuffer();
            }
            throw new SecurityException("basic 认证失败.");
        }
    }
}
