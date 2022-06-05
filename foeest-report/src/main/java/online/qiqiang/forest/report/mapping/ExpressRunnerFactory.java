package online.qiqiang.forest.report.mapping;

import com.ql.util.express.ExpressRunner;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qiqiang
 */
@Slf4j
public class ExpressRunnerFactory {

    public static ExpressRunner getExpressRunner() {
        return Instance.SINGLETON.instance;
    }

    enum Instance {
        /**
         * 单例构建
         */
        SINGLETON;
        /**
         * ExpressRunner
         */
        final ExpressRunner instance;

        Instance() {
            instance = new ExpressRunner();
            try {
                addFunctions();
            } catch (Exception e) {
                log.error("初始化 ExpressRunner 错误", e);
            }
        }

        private void addFunctions() {

        }
    }
}
