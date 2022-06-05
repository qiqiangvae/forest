package online.qiqiang.forest.common.utils.reflection;

import online.qiqiang.forest.common.exception.ReflectForestException;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 类属性工具
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class PropertyUtils {
    private static final Unsafe unsafe = PropertyUtils.getValue(FieldUtils.getField(Unsafe.class, "theUnsafe"), null);

    /**
     * 获取一个对象的字段值
     *
     * @param field  字段
     * @param object 对象
     * @param <T>    target class
     * @return field value
     */
    @SuppressWarnings("unchecked")

    public static <T> T getValue(Field field, Object object) {
        boolean accessible = field.isAccessible();
        try {
            if (!accessible) {
                field.setAccessible(true);
            }
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            throw new ReflectForestException(e);
        } finally {
            // 设置回去，保证安全
            field.setAccessible(accessible);
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

    /**
     * 设置属性
     *
     * @param field  字段
     * @param object 对象
     * @param value  新值
     */
    public static void setValue(Field field, Object object, Object value) {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new ReflectForestException(e);
        } finally {
            field.setAccessible(accessible);
        }
    }

    /**
     * 设置属性
     *
     * @param object 对象
     * @param offset 偏移量
     * @param value  新值
     */
    public static void setValueFaster(Object object, long offset, Object value) {
        unsafe.putObject(object, offset, value);
    }
}
