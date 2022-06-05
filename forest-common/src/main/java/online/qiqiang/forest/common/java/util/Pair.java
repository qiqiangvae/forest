package online.qiqiang.forest.common.java.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author qiqiang
 */
@AllArgsConstructor
@Data
public abstract class Pair<L, R> {
    private L left;
    private R right;
}
