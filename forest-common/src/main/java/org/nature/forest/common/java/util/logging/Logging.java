package org.nature.forest.common.java.util.logging;

import org.nature.forest.common.java.util.function.NothingConsumer;
import org.slf4j.Logger;


/**
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class Logging {
    public static void debug(Logger logger, NothingConsumer consumer) {
        if (logger.isDebugEnabled()) {
            consumer.accept();
        }
    }

    public static void info(Logger logger, NothingConsumer consumer) {
        if (logger.isInfoEnabled()) {
            consumer.accept();
        }
    }

    public static void warn(Logger logger, NothingConsumer consumer) {
        if (logger.isWarnEnabled()) {
            consumer.accept();
        }
    }

    public static void error(Logger logger, NothingConsumer consumer) {
        if (logger.isErrorEnabled()) {
            consumer.accept();
        }
    }

    public static void trace(Logger logger, NothingConsumer consumer) {
        if (logger.isTraceEnabled()) {
            consumer.accept();
        }
    }
}
