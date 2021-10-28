package online.qiqiang.forest.framework.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import online.qiqiang.forest.framework.log.service.LogPackagePathTestService;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

/**
 * @author qiqiang
 */
public class LogTest {

    /**
     * 测试类级别
     */
    @Test
    public void testLog1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        System.setProperty("forest.log.enable", "true");
        beanDefinition.setBeanClass(LogClassLevelTestService.class);
        context.register(LogPrinterAutoConfiguration.class);
        context.registerBeanDefinition("logTestService", beanDefinition);
        context.registerBean(ObjectMapper.class);
        context.refresh();
        LogClassLevelTestService testService = context.getBean(LogClassLevelTestService.class);
        testService.test(getTestModel(), "test");
    }

    private TestModel getTestModel() {
        TestModel testModel = new TestModel();
        testModel.setAge(1);
        testModel.setBirthday(new Date());
        testModel.setName("forest");
        return testModel;
    }

    /**
     * 方法级别测试
     */
    @Test
    public void testLog2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        System.setProperty("forest.log.enable", "true");
        beanDefinition.setBeanClass(LogMethodLevelTestService.class);
        context.register(LogPrinterAutoConfiguration.class);
        context.registerBeanDefinition("logTestService", beanDefinition);
        context.registerBean(ObjectMapper.class);
        context.refresh();
        LogMethodLevelTestService testService = context.getBean(LogMethodLevelTestService.class);
        testService.test(getTestModel(), "test");
    }

    /**
     * package path 级别测试
     */
    @Test
    public void testLog3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        System.setProperty("forest.log.enable", "true");
        beanDefinition.setBeanClass(LogPackagePathTestService.class);
        context.register(LogPrinterAutoConfiguration.class);

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ForestLogProperties.class);
        beanDefinitionBuilder.addPropertyValue("enable", true);
        beanDefinitionBuilder.addPropertyValue("ignoreReq", "testModel.age");
        beanDefinitionBuilder.addPropertyValue("ignoreResp", "name");
        beanDefinitionBuilder.addPropertyValue("packagePath", "online.qiqiang.forest.framework.log.service");

        context.registerBeanDefinition("forestLogProperties", beanDefinitionBuilder.getBeanDefinition());
        context.registerBeanDefinition("logTestService", beanDefinition);
        context.registerBean(ObjectMapper.class);
        context.refresh();
        LogPackagePathTestService testService = context.getBean(LogPackagePathTestService.class);
        testService.test(getTestModel(), "test");
    }
}
