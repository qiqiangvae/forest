package org.nature.forest.framework.thread;

import org.nature.forest.framework.context.ForestContext;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author qiqiang
 */
public class ContextCallable<V> implements Callable<V> {

    private final Callable<V> callable;
    private final Map<String, Object> context;

    public ContextCallable(Callable<V> callable) {
        this.callable = callable;
        context = ForestContext.getAll();
    }

    @Override
    public V call() throws Exception {
        try {
            ForestContext.reset(context);
            return callable.call();
        } finally {
            ForestContext.clear();
        }
    }
}
