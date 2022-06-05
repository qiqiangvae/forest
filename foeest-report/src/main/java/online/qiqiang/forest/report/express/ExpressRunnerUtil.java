package online.qiqiang.forest.report.express;

import com.ql.util.express.IExpressContext;
import lombok.SneakyThrows;
import online.qiqiang.forest.report.mapping.ExpressRunnerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qiqiang
 */
@Component
public class ExpressRunnerUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 通用导入包引用
     */
    public static final List<String> COMMON_CLASS_IMPORT;

    static {
        COMMON_CLASS_IMPORT = new ArrayList<>();
    }

    /**
     * execute
     *
     * @param express 表达式
     * @param context 上下文
     * @return f
     * @throws Exception
     */
    @SneakyThrows
    public Object execute(String express, Map<String, Object> context) {
        IExpressContext<String, Object> expressContext = new ExpressRunnerContext(context, applicationContext);
        return ExpressRunnerFactory.getExpressRunner().execute(express, expressContext, null, true, false);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
