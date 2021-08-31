package org.qiqiang.forest.common.utils.reflection;

import org.qiqiang.forest.common.exception.ReflectForestException;

import java.lang.reflect.Field;

/**
 * @author qiqiang
 */
public class PropertyUtils {
    public static Object getValue(Field field, Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new ReflectForestException(e);
        }
    }
}
