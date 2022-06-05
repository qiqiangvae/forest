package online.qiqiang.forest.report.express;

import com.ql.util.express.IExpressContext;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiqiang
 */
public class ExpressRunnerContext extends HashMap<String, Object> implements IExpressContext<String, Object> {

    private final ApplicationContext applicationContext;

    public ExpressRunnerContext(Map<String, Object> expressContext, ApplicationContext applicationContext) {
        super(expressContext);
        this.applicationContext = applicationContext;
    }

    @Override
    public Object get(Object name) {
        Object result;
        result = super.get(name);
        try {
            if (result == null && this.applicationContext != null && this.applicationContext.containsBean((String) name)) {
                // 如果在Spring容器中包含bean，则返回String的Bean
                result = this.applicationContext.getBean((String) name);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }
}
