package online.qiqiang.forest.orm.mybatis.log;

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
    public boolean supportCommand(SqlCommandType sqlCommandType) {
        return mybatisLoggerProperties.getSqlCommandType() != null
                && mybatisLoggerProperties.getSqlCommandType().contains(sqlCommandType.name());
    }

    @Override
    public boolean supportSqlId(String sqlId) {
        return mybatisLoggerProperties.getSqlIds() != null && mybatisLoggerProperties.getSqlIds().contains(sqlId);
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
