package online.qiqiang.forest.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

    private final boolean ignoreEmpty;
}
