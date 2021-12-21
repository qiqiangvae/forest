package online.qiqiang.forest.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * @author qiqiang
 */
@Getter
@Builder
@AllArgsConstructor
public class ConditionWrapper {
    /**
     * 表达式
     */
    private final Express express;
    /**
     * 列名
     */
    private final String col;

    /**
     * 是否忽略空值
     */
    private final boolean ignoreEmpty;

    public static boolean ignore(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String && StringUtils.isBlank((String) value)) {
            return true;
        }
        return value instanceof Collection && CollectionUtils.isEmpty((Collection<?>) value);
    }
}
