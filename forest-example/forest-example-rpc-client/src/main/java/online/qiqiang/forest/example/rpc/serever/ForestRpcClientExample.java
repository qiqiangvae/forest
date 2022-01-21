package online.qiqiang.forest.example.rpc.serever;

import online.qiqiang.forest.common.utils.JsonUtils;
import online.qiqiang.forest.example.rpc.api.UserRpc;
import online.qiqiang.forest.rpc.core.annotation.ForestReference;
import online.qiqiang.forest.rpc.spring.consumer.EnableForestRpcConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.task.TaskExecutor;

/**
 * @author qiqiang
 */
@EnableForestRpcConsumer
@SpringBootApplication
public class ForestRpcClientExample {

    @ForestReference
    private UserRpc userRpc;

    public static void main(String[] args) {
        SpringApplication.run(ForestRpcClientExample.class);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void run(ApplicationStartedEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        TaskExecutor taskExecutor = applicationContext.getBean(TaskExecutor.class);
        for (int i = 0; i < 200; i++) {
            int index = i;
            taskExecutor.execute(() -> {
                String response = userRpc.username(String.valueOf(index));
                System.out.println(JsonUtils.write2String(response));
            });
        }
    }
}
