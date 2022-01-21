package online.qiqiang.forest.example.rpc.serever;

import online.qiqiang.forest.rpc.spring.provider.EnableForestRpcProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qiqiang
 */
@EnableForestRpcProvider
@SpringBootApplication
public class ForestRpcServerExample {
    public static void main(String[] args) {
        SpringApplication.run(ForestRpcServerExample.class);
    }
}
