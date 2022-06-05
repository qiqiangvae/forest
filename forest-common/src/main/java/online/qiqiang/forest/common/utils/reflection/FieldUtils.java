package online.qiqiang.forest.common.utils.reflection;

import sun.misc.Unsafe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class FieldUtils {
    private static final Map<Class<?>, List<Field>> CACHE = new ConcurrentHashMap<>();
    private static final Unsafe unsafe = PropertyUtils.getValue(FieldUtils.getField(Unsafe.class, "theUnsafe"), null);

    /**
     * 获取一个类及其父类的所有字段
     *
     * @param clazz Class
     * @return field set
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        // 获取自己的字段
        List<Field> all = getFields(clazz);
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            all.addAll(getAllFields(superclass));
        }
        return all;
    }

    /**
     * 获取一个类自己的字段
     *
     * @param clazz Class
     * @return field set
     */
    public static List<Field> getFields(Class<?> clazz) {
        return CACHE.computeIfAbsent(clazz, cls -> Stream.of(clazz.getDeclaredFields()).collect(Collectors.toList()));
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

    /**
     * 获取被注解标志的字段
     *
     * @param clazz           Class
     * @param annotationClass field name
     * @return field set
     */
    public static List<Field> getField(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return getFields(clazz).stream().filter(cls -> cls.getAnnotation(annotationClass) != null).collect(Collectors.toList());
    }

    public static long offset(Class<?> clazz, String fieldName) {
        return unsafe.objectFieldOffset(FieldUtils.getField(clazz, fieldName));
    }
}
