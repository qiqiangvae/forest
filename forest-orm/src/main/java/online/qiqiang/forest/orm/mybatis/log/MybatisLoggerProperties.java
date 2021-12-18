package online.qiqiang.forest.orm.mybatis.log;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.utils.JsonUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;

/**
 * @author qiqiang
 */
@Getter
@Setter
@ConfigurationProperties(prefix = MybatisLoggerProperties.PREFIX)
@Slf4j
public class MybatisLoggerProperties {
    static final String PREFIX = "forest.mybatis-log";

    /**
     * 是否启用
     */
    private boolean enable = false;

    /**
     * 支持的命令类型
     */
    private Set<String> sqlCommandType = Collections.emptySet();

    /**
     * 需要打印的 sql id
     */
    private Set<String> sqlIds = Collections.emptySet();

    /**
     * 打印的最大sql长度，默认512
     */
    private int maxLength = 512;

    /**
     * 慢查询时间，默认3s
     */
    private int slowSqlTime = 3000;

    @PostConstruct
    public void postConstruct() {
        log.info("mybatis log properties:{}", JsonUtils.write2String(this));
    }
}
