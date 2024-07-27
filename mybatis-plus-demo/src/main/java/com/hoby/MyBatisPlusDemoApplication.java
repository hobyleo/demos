package com.hoby;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liaozh
 * @since 2024-07-27
 */
@MapperScan("com.hoby.mapper")
@SpringBootApplication
public class MyBatisPlusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBatisPlusDemoApplication.class, args);
    }
}
