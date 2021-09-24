package org.nature.forest.common.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.nature.forest.common.exception.BeanCopyForestException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * bean 转换工具类
 *
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class BeanUtils {

    /**
     * @param source 源数据
     * @param target 目标数据
     * @param <S>    source class
     * @param <T>    target class
     */
    public static <S, T> void copy(S source, T target) {
        BaseEnhanceCopier beanCopier = BaseEnhanceCopier.create(source.getClass(), target.getClass(), false);
        beanCopier.copy(source, target);
    }

    public static <S, T> T copy(S source, Class<T> targetClass) {
        try {
            Constructor<T> constructor = targetClass.getConstructor();
            T target = constructor.newInstance();
            copy(source, target);
            return target;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanCopyForestException("Bean Copy Error", e);
        }
    }

    /**
     * @param sourceList  源数据集合
     * @param targetClass 目标类
     * @param <S>         source class
     * @param <T>         target class
     * @return 目标集合
     */
    public static <S, T> List<T> copy(List<S> sourceList, Class<T> targetClass) {
        return copy(sourceList, targetClass, null);
    }

    /**
     * 复制 bean 到目标类
     *
     * @param sourceList  源数据集合
     * @param targetClass 目标类
     * @param consumer    后置处理
     * @param <S>         source class
     * @param <T>         target class
     * @return 目标集合
     */
    public static <S, T> List<T> copy(List<S> sourceList, Class<T> targetClass, BiConsumer<S, T> consumer) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        List<T> targetList = new ArrayList<>(sourceList.size());
        T target;
        try {
            // 此处有争议，是否需要保证集合内的数据都是同一类型
            S s = sourceList.get(0);
            BaseEnhanceCopier beanCopier = BaseEnhanceCopier.create(s.getClass(), targetClass, false);
            for (S source : sourceList) {
                Constructor<T> constructor = targetClass.getConstructor();
                target = constructor.newInstance();
                beanCopier.copy(source, target);
                if (consumer != null) {
                    consumer.accept(source, target);
                }
                targetList.add(target);
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanCopyForestException("Bean Copy Error", e);
        }
        return targetList;
    }
}
