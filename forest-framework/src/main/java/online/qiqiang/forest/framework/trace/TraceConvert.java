package online.qiqiang.forest.framework.trace;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import online.qiqiang.forest.framework.context.ForestContext;

/**
 * 日志
 *
 * @author qiqiang
 */
public class TraceConvert extends ClassicConverter {


    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        return ForestContext.get(TraceConstants.TRACE_ID);
    }
}
