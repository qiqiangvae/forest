package org.qiqiang.forest.mvc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

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
    /**
     * 是否开启切面日志记录
     */
    private Boolean enableLog;
    /**
     * 日志忽略字段提示文本
     */
    private String logIgnoreText;

    /**
     * 全局忽略请求字段
     */
    private Set<String> logIgnoreReq;

    /**
     * 全局忽略返回字段
     */
    private Set<String> logIgnoreResp;
}
