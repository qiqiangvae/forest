package online.qiqiang.forest.common.utils.reflection;

import online.qiqiang.forest.common.exception.ReflectForestException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiqiang
 */
public class MethodUtils {
    private static final Map<Class<?>, Set<Method>> CACHE = new ConcurrentHashMap<>();

    /**
     * 获取一个类及其父类的所有字段
     *
     * @param clazz Class
     * @return method set
     */
    public static Set<Method> getAllMethods(Class<?> clazz) {
        // 获取自己的字段
        Set<Method> all = getMethods(clazz);
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            all.addAll(getAllMethods(superclass));
        }
        return all;
    }

    /**
     * 获取一个类自己的方法
     *
     * @param clazz Class
     * @return method set
     */
    public static Set<Method> getMethods(Class<?> clazz) {
        return CACHE.computeIfAbsent(clazz, cls -> Stream.of(clazz.getDeclaredMethods()).collect(Collectors.toSet()));
    }

    /**
     * 获取指定方法
     *
     * @param clazz      Class
     * @param methodName method name
     * @return method
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... types) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, types);
        } catch (NoSuchMethodException e) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                method = getMethod(superclass, methodName, types);
            }
        }
        return method;
    }

    /**
     * 获取被注解标志的字段
     *
     * @param clazz           Class
     * @param annotationClass field name
     * @return method set
     */
    public static Set<Method> getMethod(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return getAllMethods(clazz).stream().filter(cls -> cls.getAnnotation(annotationClass) != null).collect(Collectors.toSet());
    }

    public static Object invoke(Object object, Method method, Object args) {
        try {
            return method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ReflectForestException(e);
        }
    }


}
