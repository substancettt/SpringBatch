package com.wellshang;

import org.springframework.boot.SpringApplication;

import com.wellshang.config.MyLoaderConfigurer;

public class PersonLoader {
    public static void main(String[] args) {
        SpringApplication.run(MyLoaderConfigurer.class, args);
    }
}
