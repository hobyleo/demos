package com.hoby.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author hoby
 * @since 2024-04-11
 */
@Configuration
public class AsyncConfig {

    @Bean(name = "exampleExecutor")
    public Executor exampleExecutor() {
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        int processNum = Runtime.getRuntime().availableProcessors(); // 返回可用处理器的数量
        int corePoolSize = (int) (processNum / (1 - 0.2));
        int maxPoolSize = (int) (processNum / (1 - 0.5));
        threadPoolExecutor.setCorePoolSize(corePoolSize); // 核心线程数
        threadPoolExecutor.setMaxPoolSize(maxPoolSize); // 最大线程数
        threadPoolExecutor.setQueueCapacity(maxPoolSize * 1000); // 队列长度
        threadPoolExecutor.setThreadPriority(Thread.MAX_PRIORITY); // 线程优先级
        threadPoolExecutor.setDaemon(false); // 是否守护线程
        threadPoolExecutor.setKeepAliveSeconds(300); // 非核心线程空闲时间
        threadPoolExecutor.setThreadNamePrefix("example-executor-"); // 线程名字前缀
        return threadPoolExecutor;
    }

}
