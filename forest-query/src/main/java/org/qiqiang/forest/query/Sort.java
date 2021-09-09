package org.qiqiang.forest.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排序注解
 *
 * @author qiqiang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Sort {
    /**
     * 排序
     *
     * @return SortColumn.Sort
     */
    SortColumn.Sort sort() default SortColumn.Sort.Asc;

    /**
     * 字段
     *
     * @return 字段名
     */
    String col() default "";
}
