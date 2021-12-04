package online.qiqiang.forest.security.aspect;

import online.qiqiang.forest.security.callback.AuthenticateCallback;
import online.qiqiang.forest.security.support.UserSupport;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author qiqiang
 */
public abstract class BaseAspect {

    protected UserSupport userSupport;
    protected AuthenticateCallback authenticateCallback;

    public void setAuthenticateCallback(AuthenticateCallback authenticateCallback) {
        this.authenticateCallback = authenticateCallback;
    }

    public void setUserSupport(UserSupport userSupport) {
        this.userSupport = userSupport;
    }

    protected Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    protected HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        return sra.getRequest();
    }

    protected HttpServletResponse getResponse() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        return sra.getResponse();
    }
}
