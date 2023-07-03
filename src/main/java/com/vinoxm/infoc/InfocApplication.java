package com.vinoxm.infoc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.vinoxm.infoc.dao")
public class InfocApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfocApplication.class, args);
    }

}
