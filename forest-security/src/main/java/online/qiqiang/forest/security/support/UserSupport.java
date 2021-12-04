package online.qiqiang.forest.security.support;

/**
 * @author qiqiang
 */
public interface UserSupport {

    SecurityUser defaultUser();

    SecurityUser findUser(String username);
}
