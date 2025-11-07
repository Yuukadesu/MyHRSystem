package com.example.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 权限管理服务启动类
 */
@SpringBootApplication(scanBasePackages = "com.example")
public class AuthorizationManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationManagementApplication.class, args);
    }
}

