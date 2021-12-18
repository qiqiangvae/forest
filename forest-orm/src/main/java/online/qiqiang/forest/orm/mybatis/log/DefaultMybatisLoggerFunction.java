package online.qiqiang.forest.orm.mybatis.log;

import lombok.RequiredArgsConstructor;

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
    public Set<String> sqlCommandTypes() {
        return mybatisLoggerProperties.getSqlCommandType();
    }

    @Override
    public Set<String> sqlIds() {
        return mybatisLoggerProperties.getSqlIds();
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
