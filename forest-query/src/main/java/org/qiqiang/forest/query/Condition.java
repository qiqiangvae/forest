package org.qiqiang.forest.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qiqiang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Condition {
    /**
     * 条件表达式
     *
     * @return Express
     */
    Express express() default Express.equals;

    /**
     * 字段
     *
     * @return 字段名
     */
    String col() default "";
}
