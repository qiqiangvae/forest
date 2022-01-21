package online.qiqiang.forest.rpc.spring.provider;

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
@ImportAutoConfiguration(ForestRpcServerAutoConfiguration.class)
@Import(ForestServiceExposeBeanPostProcessor.class)
public @interface EnableForestRpcProvider {

}
