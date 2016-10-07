package com.wellshang;

import org.springframework.boot.SpringApplication;

import com.wellshang.config.StudentLoaderConfigurer;

public class StudentLoader {
    public static void main(String[] args) {
        SpringApplication.run(StudentLoaderConfigurer.class, args);
    }
}
