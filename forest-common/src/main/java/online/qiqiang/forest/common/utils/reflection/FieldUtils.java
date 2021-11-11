package online.qiqiang.forest.common.utils.reflection;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiqiang
 */
public class FieldUtils {

    /**
     * 获取一个类及其父类的所有字段
     *
     * @param clazz Class
     * @return field set
     */
    public static Set<Field> getAllFields(Class<?> clazz) {
        // 获取自己的字段
        Set<Field> fields = getFields(clazz);
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            fields.addAll(getAllFields(superclass));
        }
        return fields;
    }

    /**
     * 获取一个类自己的字段
     *
     * @param clazz Class
     * @return field set
     */
    public static Set<Field> getFields(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields()).collect(Collectors.toSet());
    }

    /**
     * 获取指定字段
     *
     * @param clazz     Class
     * @param fieldName field name
     * @return field
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                field = getField(superclass, fieldName);
            }
        }
        return field;
    }
}
