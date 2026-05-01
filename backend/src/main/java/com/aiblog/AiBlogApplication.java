package com.aiblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI博客系统启动类
 */
@SpringBootApplication
@MapperScan("com.aiblog.mapper")
public class AiBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiBlogApplication.class, args);
        System.out.println("=== AI博客系统启动成功 ===");
    }
}
