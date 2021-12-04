package online.qiqiang.forest.security.aspect;

import online.qiqiang.forest.common.utils.codec.Base64Utils;
import online.qiqiang.forest.security.support.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * basic 认证拦截
 *
 * @author qiqiang
 */
@Aspect
public class BasicAspect extends BaseAspect {

    @Pointcut("@annotation(online.qiqiang.forest.security.annotation.Basic)")
    public void basicPointcut() {

    }

    @Before("basicPointcut()")
    public void before() {
        HttpServletRequest request = getRequest();
        HttpServletResponse response = getResponse();
        String base6AuthStr = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (base6AuthStr == null) {
            authenticateCallback.error(request, response);
        } else {
            String userInfo = base6AuthStr.substring(6);
            String[] userInfoArray = userInfo.split(":", 2);
            SecurityUser user = userSupport.defaultUser();
            if (user == null) {
                user = userSupport.findUser(userInfoArray[0]);
            }
            if (user == null) {
                authenticateCallback.error(request, response);
            } else {
                String encode = Base64Utils.encode(user.getUsername() + ":" + user.getPassword());
                if (!StringUtils.equalsIgnoreCase(encode, userInfo)) {
                    authenticateCallback.error(request, response);
                } else {
                    authenticateCallback.success(request, response);
                }
            }
        }
    }
}
