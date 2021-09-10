package org.qiqiang.forest.common.utils.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author qiqiang
 */
public class AnnotationUtils {
    public static <T extends Annotation> T getAnnotation(Field field, Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    @SuppressWarnings("unused")
    public static <T extends Annotation> T[] getAnnotations(Field field, Class<T> annotationClass) {
        return field.getAnnotationsByType(annotationClass);
    }

    @SuppressWarnings("unused")
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
        return clazz.getAnnotation(annotationClass);
    }

    @SuppressWarnings("unused")
    public static <T extends Annotation> T[] getAnnotations(Class<?> clazz, Class<T> annotationClass) {
        return clazz.getAnnotationsByType(annotationClass);
    }
}
