package org.qiqiang.forest.framework.context;

/**
 * @author qiqiang
 */
public enum RemoteContextType {
    /**
     * 默认模式
     */
    simple,
    /**
     * redis 实现
     */
    redis,
    /**
     * zookeeper 实现，保留方案
     */
    zookeeper
}
