package online.qiqiang.forest.mvc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiqiang
 */
@ConfigurationProperties(prefix = ForestMvcProperties.PREFIX)
@Getter
@Setter
public class ForestMvcProperties {
    static final String PREFIX = "forest.mvc";
    /**
     * 是否开启 xss 攻击拦截
     */
    private Boolean enableXss;

    /**
     * 是否开启 日志追踪 traceId. 还需要注意修改logback.xml中<appender>-><encoder>节点下加上 [%X{traceId}]
     */
    private Boolean enableTrace;

}
