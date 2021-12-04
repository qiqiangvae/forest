package online.qiqiang.forest.security.callback;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 鉴权回调接口
 *
 * @author qiqiang
 */
public interface AuthenticateCallback {
    /**
     * 鉴权成功
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    default void success(HttpServletRequest request, HttpServletResponse response) {
    }

    /**
     * 鉴权失败
     * 如果需要终止执行，请主动抛出异常
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    default void error(HttpServletRequest request, HttpServletResponse response) throws SecurityException {
    }
}
