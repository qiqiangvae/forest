package online.qiqiang.forest.orm.mybatis.log;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.util.AntPathMatcher;

/**
 * @author qiqiang
 */
@RequiredArgsConstructor
public class DefaultMybatisLoggerFunction implements MybatisLoggerFunction {

    private final MybatisLoggerProperties mybatisLoggerProperties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher(".");

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
        return CollectionUtils.isNotEmpty(mybatisLoggerProperties.getSqlIds())
                && mybatisLoggerProperties.getSqlIds().stream().anyMatch(path -> pathMatcher.match(path, sqlId));
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
