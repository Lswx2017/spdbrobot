package com.jn.redistudy.spdbrobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@tk.mybatis.spring.annotation.MapperScan("com.jn.redistudy.spdbrobot.mapper")
//@ServletComponentScan
public class SpdbrobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpdbrobotApplication.class, args);
    }
}
