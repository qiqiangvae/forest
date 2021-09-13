package org.qiqiang.forest.framework.log;

/**
 * 支持返回对象重写打印日志
 * 比如：分页信息的集合，可以选择在全局配置里忽略，但是如果想展示一些自定义的信息，可以重新该结果，或者加个自定义字段也行
 *
 * @author qiqiang
 */
public interface LogResponseWriter {

    /**
     * 重写返回结果
     *
     * @param result 返回
     * @param <R>    重写的结果
     * @return 重写的结果
     */
    Object write(Object result);
}
