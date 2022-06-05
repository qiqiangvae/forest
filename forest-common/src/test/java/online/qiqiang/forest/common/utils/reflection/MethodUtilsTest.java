package online.qiqiang.forest.common.utils.reflection;

import cn.hutool.core.date.StopWatch;
import junit.framework.TestCase;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MethodUtilsTest extends TestCase {


    public void testGetAllMethods() {
    }

    public void testGetMethods() {
    }

    public void testGetMethod() throws Exception {
        int count = 10000_0000;
        StopWatch stopWatch = StopWatch.create("test");
        stopWatch.start("field");
        Field field = FieldUtils.getField(Model.class, "field");
        field.setAccessible(true);
        for (int i = 0; i < count; i++) {
            Model model = new Model();
            PropertyUtils.setValue(field, model, "1");
        }
        stopWatch.stop();
        stopWatch.start("setter invoke");
        Method method = MethodUtils.getMethod(Model.class, "setField", String.class);
        for (int i = 0; i < count; i++) {
            Model model = new Model();
            MethodUtils.invoke(model, method, "1");
        }
        stopWatch.stop();
        stopWatch.start("setter");
        for (int i = 0; i < count; i++) {
            Model model = new Model();
            model.setField("1");
        }
        stopWatch.stop();
        stopWatch.start("unsafe");
        long offset = FieldUtils.offset(Model.class, "field");
        for (int i = 0; i < count; i++) {
            Model model = new Model();
            PropertyUtils.setValueFaster(model, offset, "1");
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    public void testTestGetMethod() {
    }


    @Setter
    static class Model {
        private String field;
    }
}