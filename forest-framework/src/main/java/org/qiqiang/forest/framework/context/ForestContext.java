package org.qiqiang.forest.framework.context;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 上下文
 *
 * @author qiqiang
 */
@SuppressWarnings({"unchecked", "unused"})
public class ForestContext {
    static final String PREFIX = "@remote:";

    /**
     * 序列号
     */
    private static final AtomicLong SEQUENCE_NUMBER = new AtomicLong(0);

    @Setter
    private static RemoteContext remoteContext;
    @Setter
    private ForestContextProperties forestContextProperties;

    private static final ForestContextThreadLocal CONTEXT = new ForestContextThreadLocal();

    /**
     * 设置上下文
     */
    public static void set(String key, Object value) {
        Map<String, Object> data = CONTEXT.get();
        data.compute(ForestContextConstants.SEQUENCE, (k, old) -> StringUtils.isBlank((CharSequence) old) ? generateSequence() : old);
        data.put(key, value);
    }

    /**
     * 获取上下文
     */
    public static <T> T get(String key) {
        return (T) CONTEXT.get().get(key);
    }

    /**
     * 设置远程上下文
     */
    public static void setRemote(String key, String value) {
        set(PREFIX + key, value);
        remoteContext.set(key, value);
    }

    /**
     * 获取远程上下文
     */
    public static String getRemote(String key) {
        return remoteContext.get(key);
    }

    public static String getSequence() {
        return (String) CONTEXT.get().compute(ForestContextConstants.SEQUENCE, (k, old) -> StringUtils.isBlank((CharSequence) old) ? generateSequence() : old);
    }

    private static String generateSequence() {
        // sequence#timestamp
        return SEQUENCE_NUMBER.incrementAndGet() + "#" + System.currentTimeMillis();
    }

    public static String getRemoteSequence() {
        return PREFIX + getSequence();
    }

    /**
     * 最终必须要调用此方法，防止内存泄漏
     */
    public static void clear() {
        CONTEXT.remove();
        remoteContext.clear();
    }

    private static class ForestContextThreadLocal extends ThreadLocal<Map<String, Object>> {

        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>(8);
        }
    }
}
