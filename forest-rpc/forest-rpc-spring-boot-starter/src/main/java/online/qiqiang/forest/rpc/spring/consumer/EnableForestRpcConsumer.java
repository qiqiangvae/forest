package online.qiqiang.forest.rpc.spring.consumer;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qiqiang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ImportAutoConfiguration(ForestRpcClientAutoConfiguration.class)
@Import(ForestReferenceAutowiredAnnotationBeanPostProcessor.class)
public @interface EnableForestRpcConsumer {

}
