package org.nature.forest.common.java.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author qiqiang
 */
@SuppressWarnings("unused")
public final class OptionalCollection<T> {
    private static final OptionalCollection<?> EMPTY = new OptionalCollection<>();

    private final Collection<T> value;

    private OptionalCollection() {
        this.value = new ArrayList<>();
    }

    public static <T> OptionalCollection<T> empty() {
        @SuppressWarnings("unchecked")
        OptionalCollection<T> t = (OptionalCollection<T>) EMPTY;
        return t;
    }

    private OptionalCollection(Collection<T> value) {
        this.value = Objects.requireNonNull(value);
    }

    public static <T> OptionalCollection<T> of(Collection<T> value) {
        return new OptionalCollection<>(value);
    }

    public static <T> OptionalCollection<T> ofNullable(Collection<T> value) {
        return value == null ? empty() : of(value);
    }

    public Collection<T> get() {
        return value;
    }

    public boolean isNotEmpty() {
        return !value.isEmpty();
    }

    public void isNotEmpty(Consumer<? super Collection<T>> consumer) {
        if (isNotEmpty()) {
            consumer.accept(value);
        }
    }

    public Collection<T> orElse(Collection<T> other) {
        return !isNotEmpty() ? value : other;
    }

    public Collection<T> orElseGet(Supplier<? extends Collection<T>> other) {
        return !isNotEmpty() ? value : other.get();
    }

    public <X extends Throwable> Collection<T> orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (!isNotEmpty()) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof OptionalCollection)) {
            return false;
        }

        OptionalCollection<?> other = (OptionalCollection<?>) obj;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }


    @Override
    public String toString() {
        return !isNotEmpty()
                ? String.format("OptionalCollection[%s]", value)
                : "OptionalCollection.empty";
    }
}
