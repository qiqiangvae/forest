package org.nature.forest.framework.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qiqiang
 */
@ConfigurationProperties(prefix = ForestContextProperties.PREFIX)
@Getter
@Setter
public class ForestContextProperties {
    static final String PREFIX = "forest.context";

    private RemoteContextType type;
}
