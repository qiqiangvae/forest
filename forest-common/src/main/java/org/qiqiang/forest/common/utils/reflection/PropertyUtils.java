package org.qiqiang.forest.common.utils.reflection;

import org.qiqiang.forest.common.exception.ReflectForestException;

import java.lang.reflect.Field;

/**
 * 类属性工具
 *
 * @author qiqiang
 */
public class PropertyUtils {
    /**
     * 获取一个对象的字段值
     *
     * @param field  字段
     * @param object 对象
     * @param <T>    target class
     * @return field value
     */
    public static <T> T getValue(Field field, Object object) {
        field.setAccessible(true);
        try {
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            throw new ReflectForestException(e);
        }
    }

    /**
     * 获取一个对象的字段值
     *
     * @param fieldName 字段名
     * @param object    对象
     * @param <T>       target class
     * @return field value
     */
    public static <T> T getValue(String fieldName, Object object) {
        Field field = FieldUtils.getField(object.getClass(), fieldName);
        return getValue(field, object);
    }
}
