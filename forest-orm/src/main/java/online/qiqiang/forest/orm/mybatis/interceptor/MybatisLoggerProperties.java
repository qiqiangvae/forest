package online.qiqiang.forest.orm.mybatis.interceptor;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Set;

/**
 * @author qiqiang
 */
@EnableConfigurationProperties(MybatisLoggerProperties.class)
@Getter
@Setter
public class MybatisLoggerProperties implements MybatisLoggerPropertiesFunction {
    @Value("${forest.mybatis-log.enable:false}")
    private boolean enable;

    @Value("${forest.mybatis-log.sql-command-type}")
    private Set<SqlCommandType> sqlCommandType;

    /**
     * 打印的最大sql长度，默认1024
     */
    @Value("${forest.mybatis-log.max-length:512}")
    private int maxLength;

    /**
     * 慢查询时间，默认5s
     */
    @Value("${forest.mybatis-log.slow-sql-time:3000}")
    private int slowSqlTime;

    @Override
    public boolean enable() {
        return enable;
    }

    @Override
    public Set<SqlCommandType> supportedCommandTypes() {
        return sqlCommandType;
    }

    @Override
    public int maxlength() {
        return maxLength;
    }

    @Override
    public int slowSqlTime() {
        return slowSqlTime;
    }
}
