package com.hoby;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liaozh
 * @since 2024-07-12
 */
@SpringBootApplication
@MapperScan("com.hoby.mapper")
public class ShardingSphereDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingSphereDemoApplication.class, args);
    }
}
