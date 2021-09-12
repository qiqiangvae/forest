package org.qiqiang.forest.framework.log;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志记录打印
 * 在类上声明，表示该类下所有的方法的请求都会被记录
 * 在方法上声明，表示该方法的请求会被记录
 * 可在配置文件中配置全局忽略字段和忽略字段提示 {@linkplain ForestLogProperties}
 *
 * @author qiqiang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogPrinter {
    /**
     * 是否不启用
     */
    boolean enable() default true;

    /**
     * 忽略的入参字段
     */
    String[] ignoreReq() default {};

    /**
     * 忽略的出参字段，root 为 resp
     */
    String[] ignoreResp() default {};

}
