package online.qiqiang.forest.common.utils.id;


import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.utils.OsUtils;

import java.util.concurrent.atomic.AtomicLong;

import static online.qiqiang.forest.common.utils.id.SnowFlakeConstants.*;

/**
 * @author qiqiang
 */
@Slf4j
@SuppressWarnings("unused")
public final class SnowFlakeWrapper {

    private static final long DIRECT = 1452649500L;
    private static final long PID_SHIFT = CODER_SEQUENCE_FIELD_BITS;
    private static final long IP_SHIFT = CODER_PID_FIELD_BITS + PID_SHIFT;
    private static final long TYPE_SHIFT = CODER_IP_FIELD_BITS + IP_SHIFT;
    private static final long TIMESTAMP_SHIFT = CODER_IP_FIELD_BITS + TYPE_SHIFT;
    private static final long SEQUENCE_MASK = ~(-1L << CODER_SEQUENCE_FIELD_BITS);

    private static long workId = -1;

    private static final AtomicLong LAST_TIME = new AtomicLong(-1L);
    private static long sequence = 0L;

    /**
     * 根据编码获取编码类型
     *
     * @param code 编码
     * @return 编码类型int值
     */
    public static int getType(long code) {
        return (int) ((code >> TYPE_SHIFT) & 0XFF);
    }

    /**
     * 根据类型生成编码
     *
     * @param type 编码类型int值
     * @return 成功返回非负长整形的字符串, 失败返回null
     */
    public static String getId(int type) {
        long longId = SnowFlakeWrapper.getLongId(type);

        return (longId == -1) ? null : Long.toString(longId);
    }

    /**
     * 根据类型生成编码
     *
     * @param type 编码类型int值
     * @return 成功返回非负长整形, 失败返回-1
     */
    public static long getLongId(int type) {

        long code = -1;
        int loop = 0;
        do {
            try {
                code = nextId(type);
                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    log.error("", e1);
                    Thread.currentThread().interrupt();
                }
            }
        } while (loop++ < SnowFlakeConstants.GENERATE_MAX_TRIES);

        return code;
    }

    /**
     * 真正生成唯一编码的方法
     *
     * @param type 编码类型int值
     * @return 成功返回非负长整形
     */
    private synchronized static long nextId(long type) {

        long timestamp = timeGen();
        final long lastTimestamp = LAST_TIME.get();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0L) {
                timestamp = tillNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        LAST_TIME.set(timestamp);
        return ((timestamp - DIRECT) << TIMESTAMP_SHIFT) | type << TYPE_SHIFT | getWorkId() | sequence;
    }

    private static long getWorkId() {
        if (workId < 0) {
            try {
                long ip = OsUtils.getLongIp();
                ip = ip & 0XFF;
                long pid = OsUtils.getCurrentProcessId();
                pid = pid & 0X1F;
                workId = (ip << IP_SHIFT) | (pid << PID_SHIFT);
            } catch (Exception e) {
                throw new RuntimeException("init workId error with reason:" + e.getMessage());
            }
        }
        return workId;
    }

    private static long tillNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private static long timeGen() {
        return System.currentTimeMillis() / 1000;
    }

}
