package org.qiqiang.forest.mvc;

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
    Boolean enableXss;
}
