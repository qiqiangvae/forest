package org.qiqiang.forest.framework.log;

/**
 * @author qiqiang
 */
public class DefaultLogResponseWriter implements LogResponseWriter {

    @Override
    public Object write(Object result) {
        return result;
    }
}
