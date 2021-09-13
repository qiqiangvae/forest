package org.qiqiang.forest.example.mvc.logwriter;

import org.qiqiang.forest.example.mvc.vo.TimeTestVO;
import org.qiqiang.forest.example.mvc.vo.XssTestVO;
import org.qiqiang.forest.framework.log.LogResponseWriter;

/**
 * @author qiqiang
 */
public class ExampleLogWriter implements LogResponseWriter {
    @Override
    public Object write(Object result) {
        if (result instanceof TimeTestVO) {
            TimeTestVO vo = (TimeTestVO) result;
            XssTestVO xssTestVO = vo.getXssTestVO();
            xssTestVO.setContext("我是重写后的值");
        }
        return result;
    }
}
