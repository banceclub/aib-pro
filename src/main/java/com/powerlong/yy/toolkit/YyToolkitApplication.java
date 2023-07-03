package com.powerlong.yy.toolkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = "com.powerlong.yy.toolkit")
public class YyToolkitApplication {

    public static void main(String[] args) {
        SpringApplication.run(YyToolkitApplication.class, args);
    }

}
