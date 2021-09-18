package org.nature.forest.framework.log;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author qiqiang
 */
@ConfigurationProperties(prefix = ForestLogProperties.PREFIX)
@Getter
@Setter
public class ForestLogProperties {
    static final String PREFIX = "forest.log";
    /**
     * 是否开启切面日志记录
     */
    private Boolean enable;
    /**
     * 扫描包路径
     */
    private String packagePath;
    /**
     * 日志忽略字段提示文本
     */
    private String ignoreText;

    /**
     * 全局忽略请求字段
     */
    private Set<String> ignoreReq;

    /**
     * 全局忽略返回字段
     */
    private Set<String> ignoreResp;

}
