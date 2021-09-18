package org.qiqiang.forest.framework.thread;

import org.qiqiang.forest.framework.context.ForestContext;

import java.util.Map;

/**
 * @author qiqiang
 */
public class ContextRunnable implements Runnable {

    private final Runnable runnable;
    private final Map<String, Object> context;

    public ContextRunnable(Runnable runnable) {
        this.runnable = runnable;
        context = ForestContext.getAll();
    }

    @Override
    public void run() {
        try {
            ForestContext.reset(context);
            runnable.run();
        } finally {
            ForestContext.clear();
        }
    }
}
