package org.qiqiang.forest.example.mvc;

import org.qiqiang.forest.framework.thread.ContextThreadPoolExecutor;
import org.qiqiang.forest.framework.thread.ContextThreadPoolTaskExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qiqiang
 */
@SpringBootApplication
@Configuration
public class ForestExampleMvcApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForestExampleMvcApplication.class);
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        return new ContextThreadPoolTaskExecutor();
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ContextThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }
}
