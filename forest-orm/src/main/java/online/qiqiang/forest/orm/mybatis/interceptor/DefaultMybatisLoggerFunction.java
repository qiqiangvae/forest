package online.qiqiang.forest.orm.mybatis.interceptor;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Set;

/**
 * @author qiqiang
 */
@RequiredArgsConstructor
public class DefaultMybatisLoggerFunction implements MybatisLoggerFunction {

    private final MybatisLoggerProperties mybatisLoggerProperties;

    @Override
    public boolean enable() {
        return mybatisLoggerProperties.isEnable();
    }

    @Override
    public Set<SqlCommandType> supportedCommandTypes() {
        return mybatisLoggerProperties.getSqlCommandType();
    }

    @Override
    public int maxlength() {
        return mybatisLoggerProperties.getMaxLength();
    }

    @Override
    public int slowSqlTime() {
        return mybatisLoggerProperties.getSlowSqlTime();
    }
}
