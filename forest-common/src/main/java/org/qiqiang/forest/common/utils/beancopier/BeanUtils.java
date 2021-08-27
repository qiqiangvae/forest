package org.qiqiang.forest.common.utils.beancopier;

import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.qiqiang.forest.common.exception.BeanCopyForestException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * bean 转换工具类
 *
 * @author qiqiang
 */
public class BeanUtils {
    /**
     * 缓存容器
     */
    private static final Map<BeanCopierKey, BeanCopier> COPIER_MAP = new HashMap<>();

    public static <S, T> void copy(S source, T target) {
        BeanCopierKey copierKey = new BeanCopierKey(source.getClass(), target.getClass());
        BeanCopier beanCopier = COPIER_MAP.get(copierKey);
        if (beanCopier == null) {
            synchronized (COPIER_MAP) {
                beanCopier = COPIER_MAP.get(copierKey);
                if (beanCopier == null) {
                    beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
                    COPIER_MAP.put(copierKey, beanCopier);
                }
            }
        }
        beanCopier.copy(source, target, null);
    }

    public static <S, T> List<T> copy(List<S> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        List<T> targetList = new ArrayList<>(sourceList.size());
        T target;
        try {
            for (S source : sourceList) {
                Constructor<T> constructor = targetClass.getConstructor();
                target = constructor.newInstance();
                copy(source, target);
                targetList.add(target);
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanCopyForestException("Bean Copy Error", e);
        }
        return targetList;
    }

    private static class BeanCopierKey {
        Class<?> sourceClass;
        Class<?> targetClass;

        public BeanCopierKey(Class<?> sourceClass, Class<?> targetClass) {
            this.sourceClass = sourceClass;
            this.targetClass = targetClass;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BeanCopierKey that = (BeanCopierKey) o;
            return Objects.equals(sourceClass, that.sourceClass) && Objects.equals(targetClass, that.targetClass);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sourceClass, targetClass);
        }
    }

}
