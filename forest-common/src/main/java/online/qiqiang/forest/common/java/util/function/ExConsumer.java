package online.qiqiang.forest.common.java.util.function;

import online.qiqiang.forest.common.exception.Throwing;

import java.util.function.Consumer;

/**
 * 包装 Consumer 类，可以优雅的在 lambda 中抛出异常
 *
 * @author qiqiang
 */
@FunctionalInterface
public interface ExConsumer<T> extends Consumer<T> {

    /**
     * 消费
     *
     * @param e 数据
     */
    @Override
    default void accept(final T e) {
        try {
            exAccept(e);
        } catch (Throwable ex) {
            Throwing.sneakyThrow(ex);
        }
    }

    /**
     * 内部消费
     *
     * @param e 数据
     * @throws Throwable ex
     */
    void exAccept(T e) throws Throwable;
}
