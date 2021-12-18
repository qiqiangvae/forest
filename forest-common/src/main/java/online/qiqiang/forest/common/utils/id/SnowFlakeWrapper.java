package online.qiqiang.forest.common.utils.id;

import lombok.extern.slf4j.Slf4j;
import online.qiqiang.forest.common.utils.OsUtils;

import java.util.concurrent.atomic.AtomicStampedReference;

import static online.qiqiang.forest.common.utils.id.SnowFlakeConstants.*;

/**
 * @author <a href="mailto:zhujy@pingpongx.com">zhujunying</a>
 */
@Slf4j
public final class SnowFlakeWrapper {

    private static final long DIRECT = 1452649500L;
    private static final long PID_SHIFT = CODER_SEQUENCE_FIELD_BITS;
    private static final long IP_SHIFT = CODER_PID_FIELD_BITS + PID_SHIFT;
    private static final long TYPE_SHIFT = CODER_IP_FIELD_BITS + IP_SHIFT;
    private static final long TIMESTAMP_SHIFT = CODER_IP_FIELD_BITS + TYPE_SHIFT;
    private static final long SEQUENCE_MASK = ~(-1L << CODER_SEQUENCE_FIELD_BITS);
    /**
     * 起始时间, 2016-01-01
     */
    private static final long START_TIME = 1451577600000L;

    private static long workId = -1;

    private static final AtomicStampedReference<StampedSequence> CURRENT = new AtomicStampedReference<>(new StampedSequence(START_TIME, 1), 0);

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
    @SuppressWarnings("unused")
    public static String getId(int type) {
        long longId = getLongId(type);

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

    static class StampedSequence {

        private long timestamp; // 时间戳
        private long sequence; // 序列

        public StampedSequence(long stamp, long sequence) {
            super();
            this.timestamp = stamp;
            this.sequence = sequence;
        }
    }


    /**
     * 真正生成唯一编码的方法
     *
     * @param type 编码类型int值
     * @return 成功返回非负长整形
     */
    private static long nextId(long type) {
        //下一个(时间戳+序列)
        StampedSequence nextSequence = new StampedSequence(0, 0);

        //版本
        int[] versionHolder = new int[1];
        while (true) {
            long now = System.currentTimeMillis();
            StampedSequence curSequence = CURRENT.get(versionHolder);
            if (now < curSequence.timestamp) {
                throw new RuntimeException("Clock moved backwards!");
            } else if (curSequence.timestamp == now) {
                if ((curSequence.sequence & SEQUENCE_MASK) == 0) {
                    //满序列等待下一毫秒
                    continue;
                }
                nextSequence.timestamp = curSequence.timestamp;
                nextSequence.sequence = curSequence.sequence + 1;
                boolean set = CURRENT.compareAndSet(curSequence, nextSequence, versionHolder[0], versionHolder[0] + 1);
                if (!set) {
                    // 无锁更新失败，重新获取
                    continue;
                }
                break;
            } else {
                nextSequence.timestamp = now;
                nextSequence.sequence = 1;
                boolean set = CURRENT.compareAndSet(curSequence, nextSequence, versionHolder[0], versionHolder[0] + 1);
                if (!set) {
                    // 无锁更新失败，重新获取
                    continue;
                }
                break;
            }
        }

        return ((nextSequence.timestamp - DIRECT) << TIMESTAMP_SHIFT) | type << TYPE_SHIFT | getWorkId() | nextSequence.sequence;
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
}
