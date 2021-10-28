package online.qiqiang.forest.framework.context;

import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 上下文
 *
 * @author qiqiang
 */
@SuppressWarnings({"unchecked", "unused"})
public class ForestContext {

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
        data.computeIfAbsent(ForestContextConstants.SEQUENCE, k -> generateSequence());
        data.put(key, value);
    }

    /**
     * 获取上下文
     */
    public static <T> T get(String key) {
        return (T) CONTEXT.get().get(key);
    }

    public static Map<String, Object> getAll() {
        return CONTEXT.get();
    }

    /**
     * 设置远程上下文
     */
    public static void setRemote(String key, String value) {
        set(RemoteContext.REMOTE_PREFIX + key, value);
        remoteContext.set(key, value);
    }

    /**
     * 获取远程上下文
     */
    public static String getRemote(String key) {
        return remoteContext.get(key);
    }

    public static String getSequence() {
        return (String) CONTEXT.get().computeIfAbsent(ForestContextConstants.SEQUENCE, k -> generateSequence());
    }

    private static String generateSequence() {
        // sequence#timestamp
        return SEQUENCE_NUMBER.incrementAndGet() + "#" + System.currentTimeMillis();
    }

    public static String getRemoteSequence() {
        return RemoteContext.REMOTE_PREFIX + getSequence();
    }

    /**
     * 最终必须要调用此方法，防止内存泄漏
     */
    public static void clear() {
        CONTEXT.remove();
        remoteContext.clear();
    }

    public static void reset(Map<String, Object> context) {
        CONTEXT.get().clear();
        CONTEXT.get().putAll(context);

    }

    private static class ForestContextThreadLocal extends ThreadLocal<Map<String, Object>> {

        @Override
        protected Map<String, Object> initialValue() {
            return new ConcurrentHashMap<>(8);
        }
    }
}
