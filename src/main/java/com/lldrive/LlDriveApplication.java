package com.lldrive;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lldrive.mapper")
public class LlDriveApplication {
    public static void main(String[] args) {
        SpringApplication.run(LlDriveApplication.class, args);
    }
}
