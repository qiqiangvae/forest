package online.qiqiang.forest.common.java.util.function;

import online.qiqiang.forest.common.exception.Throwing;

import java.util.function.Function;

/**
 * 包装 Function 类，可以优雅的在 lambda 中抛出异常
 *
 * @author qiqiang
 */
@FunctionalInterface
public interface ExFunction<T, R> extends Function<T, R> {

    /**
     * 执行
     *
     * @param t 入参
     * @return 输出
     */
    @Override
    default R apply(T t) {
        try {
            return exApply(t);
        } catch (Throwable ex) {
            Throwing.sneakyThrow(ex);
        }
        return null;
    }

    /**
     * 内部执行
     *
     * @param t 入参
     * @return 输出
     * @throws Throwable ex
     */
    R exApply(T t) throws Throwable;
}
