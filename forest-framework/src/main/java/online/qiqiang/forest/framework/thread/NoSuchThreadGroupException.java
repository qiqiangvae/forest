package online.qiqiang.forest.framework.thread;

import online.qiqiang.forest.common.exception.BaseForestException;

/**
 * @author qiqiang
 */
public class NoSuchThreadGroupException extends BaseForestException {

    public NoSuchThreadGroupException(String group) {
        super("线程组[" + group + "]不存在，请先创建线程组.");
    }
}
