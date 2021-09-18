package org.qiqiang.forest.common.exception;

import org.qiqiang.forest.common.function.ExConsumer;
import org.qiqiang.forest.common.function.ExFunction;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author qiqiang
 */
public class Throwing {
    private Throwing() {
    }

    public static <T> Consumer<T> rethrow(final ExConsumer<T> consumer) {
        return consumer;
    }

    public static <T, R> Function<T, R> rethrow(final ExFunction<T, R> function) {
        return function;
    }

    /**
     * The compiler sees the signature with the throws T inferred to a RuntimeException type, so it allows the unchecked
     * exception to propagate.
     * <p>
     * http://www.baeldung.com/java-sneaky-throws
     */
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void sneakyThrow(final Throwable ex) throws E {
        throw (E) ex;
    }
}
