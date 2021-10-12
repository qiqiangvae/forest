package org.nature.forest.common.utils;

import org.nature.forest.common.function.VoidConsumer;
import org.slf4j.Logger;


/**
 * @author qiqiang
 */
@SuppressWarnings("unused")
public class Logging {
    public static void debug(Logger logger, VoidConsumer consumer) {
        if (logger.isDebugEnabled()) {
            consumer.accept();
        }
    }

    public static void info(Logger logger, VoidConsumer consumer) {
        if (logger.isInfoEnabled()) {
            consumer.accept();
        }
    }

    public static void warn(Logger logger, VoidConsumer consumer) {
        if (logger.isWarnEnabled()) {
            consumer.accept();
        }
    }

    public static void error(Logger logger, VoidConsumer consumer) {
        if (logger.isErrorEnabled()) {
            consumer.accept();
        }
    }

    public static void trace(Logger logger, VoidConsumer consumer) {
        if (logger.isTraceEnabled()) {
            consumer.accept();
        }
    }
}
