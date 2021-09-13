package org.qiqiang.forest.common.function;

import org.qiqiang.forest.common.exception.Throwing;

import java.util.function.Supplier;

/**
 * 包装 Supplier 类，可以优雅的在 lambda 中抛出异常
 *
 * @author qiqiang
 */
@FunctionalInterface
public interface ExSupplier<R> extends Supplier<R> {

    /**
     * 获取
     *
     * @return 输出
     */
    @Override
    default R get() {
        try {
            return innerGet();
        } catch (Throwable ex) {
            Throwing.sneakyThrow(ex);
        }
        return null;
    }

    /**
     * 内部获取
     *
     * @return 输出
     * @throws Throwable ex
     */
    R innerGet() throws Throwable;
}
