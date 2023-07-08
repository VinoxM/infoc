package com.vinoxm.infoc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.vinoxm.infoc.dao")
@EnableScheduling
public class InfocApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfocApplication.class, args);
    }

}
