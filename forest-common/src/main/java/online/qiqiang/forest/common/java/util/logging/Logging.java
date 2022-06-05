package online.qiqiang.forest.common.java.util.logging;

import lombok.SneakyThrows;
import online.qiqiang.forest.common.java.util.function.NothingConsumer;
import org.slf4j.Logger;


/**
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class Logging {
    @SneakyThrows(Exception.class)
    public static void debug(Logger logger, NothingConsumer consumer) {
        if (logger.isDebugEnabled()) {
            consumer.accept();
        }
    }

    @SneakyThrows(Exception.class)
    public static void info(Logger logger, NothingConsumer consumer) {
        if (logger.isInfoEnabled()) {
            consumer.accept();
        }
    }

    @SneakyThrows(Exception.class)
    public static void warn(Logger logger, NothingConsumer consumer) {
        if (logger.isWarnEnabled()) {
            consumer.accept();
        }
    }

    @SneakyThrows(Exception.class)
    public static void error(Logger logger, NothingConsumer consumer) {
        if (logger.isErrorEnabled()) {
            consumer.accept();
        }
    }

    @SneakyThrows(Exception.class)
    public static void trace(Logger logger, NothingConsumer consumer) {
        if (logger.isTraceEnabled()) {
            consumer.accept();
        }
    }
}
