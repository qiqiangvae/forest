package online.qiqiang.forest.orm.mybatis.log;

import org.apache.ibatis.mapping.SqlCommandType;

/**
 * 支持开发者自定义扩展，实现动态生效
 *
 * @author qiqiang
 */
public interface MybatisLoggerFunction {
    default boolean enable() {
        return false;
    }

    default boolean supportCommand(SqlCommandType sqlCommandType) {
        return false;
    }

    default boolean supportSqlId(String sqlId) {
        return false;
    }

    default int maxlength() {
        return 1024;
    }

    default int slowSqlTime() {
        return 3000;
    }
}
