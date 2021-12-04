package online.qiqiang.forest.security.callback;

import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * @author qiqiang
 */
public abstract class AbstractAuthenticateCallback implements AuthenticateCallback {
    private final static String BASIC_REALM = "basic realm=\"no auth\"";

    @SneakyThrows
    protected void write401(HttpServletResponse response) {
        assert response != null;
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, BASIC_REALM);
        response.flushBuffer();
    }
}
