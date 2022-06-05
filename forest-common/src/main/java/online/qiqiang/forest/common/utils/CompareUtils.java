package online.qiqiang.forest.common.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author qiqiang
 */
public class CompareUtils {
    /**
     * 取两者较大元素
     *
     * @param a   待比较元素
     * @param b   待比较元素
     * @param <T> Comparable
     * @return max one
     */
    public static <T extends Comparable<T>> Optional<T> max(T a, T b) {
        return Stream.of(Objects.requireNonNull(a), Objects.requireNonNull(b)).max(Comparable::compareTo);
    }

    /**
     * 取两者较小元素
     *
     * @param a   待比较元素
     * @param b   待比较元素
     * @param <T> Comparable
     * @return mini one
     */
    public static <T extends Comparable<T>> Optional<T> min(T a, T b) {
        return Stream.of(Objects.requireNonNull(a), Objects.requireNonNull(b)).min(Comparable::compareTo);
    }


    public static <T extends Comparable<T>> boolean compareEquals(T a, T b) {
        return Objects.requireNonNull(a).compareTo(Objects.requireNonNull(b)) == 0;
    }
}
