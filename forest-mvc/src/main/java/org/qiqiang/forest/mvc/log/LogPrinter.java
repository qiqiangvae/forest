package org.qiqiang.forest.mvc.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志记录点位
 *
 * @author qiqiang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogPrinter {
    /**
     * 忽略的入参字段
     */
    String[] ignoreReq() default {};

    /**
     * 忽略的出参字段，root 为 resp
     */
    String[] ignoreResp() default {};
}
