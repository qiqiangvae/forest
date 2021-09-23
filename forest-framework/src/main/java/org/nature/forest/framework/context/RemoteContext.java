package org.nature.forest.framework.context;

/**
 * 远程上下文
 *
 * @author qiqiang
 */
public interface RemoteContext {

    String REMOTE_PREFIX = "@remote:";


    /**
     * 获取
     *
     * @param key key
     * @return value
     */
    String get(String key);

    /**
     * 设置
     *
     * @param key   key
     * @param value value
     */
    void set(String key, String value);

    /**
     * 清除
     */
    void clear();
}
