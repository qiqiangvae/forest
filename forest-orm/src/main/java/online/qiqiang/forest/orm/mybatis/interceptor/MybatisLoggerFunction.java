package online.qiqiang.forest.orm.mybatis.interceptor;

import org.apache.ibatis.mapping.SqlCommandType;

import java.util.HashSet;
import java.util.Set;

/**
 * 支持开发者自定义扩展，实现动态生效
 *
 * @author qiqiang
 */
public interface MybatisLoggerFunction {
    default boolean enable() {
        return false;
    }

    default Set<SqlCommandType> supportedCommandTypes() {
        return new HashSet<>();
    }

    default int maxlength() {
        return 1024;
    }

    default int slowSqlTime() {
        return 3000;
    }
}
