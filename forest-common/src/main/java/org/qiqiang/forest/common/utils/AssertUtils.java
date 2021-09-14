package org.qiqiang.forest.common.utils;

import java.util.Collection;

/**
 * @author qiqiang
 */
@SuppressWarnings("unused")
public final class AssertUtils {

    public static void isTure(Boolean bool, String message) {
        if (!bool) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void eq(Object object1, Object object2, String message) {
        if (object1 != object2) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.size() == 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
