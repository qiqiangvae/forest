package org.qiqiang.forest.common.utils;

import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.qiqiang.forest.common.exception.BeanCopyForestException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * bean 转换工具类
 *
 * @author qiqiang
 */
public class BeanUtils {

    public static <S, T> void copy(S source, T target) {
        BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
        beanCopier.copy(source, target, null);
    }

    public static <S, T> List<T> copy(List<S> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        List<T> targetList = new ArrayList<>(sourceList.size());
        T target;
        try {
            // 此处有争议，是否需要保证集合内的数据都是同一类型
            S s = sourceList.get(0);
            BeanCopier beanCopier = BeanCopier.create(s.getClass(), targetClass, false);
            for (S source : sourceList) {
                Constructor<T> constructor = targetClass.getConstructor();
                target = constructor.newInstance();
                beanCopier.copy(source, target, null);
                targetList.add(target);
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanCopyForestException("Bean Copy Error", e);
        }
        return targetList;
    }
}
