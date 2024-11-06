package com.xust.ffms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Hp
 */
@MapperScan("com.xust.ffms.dao")
@SpringBootApplication
public class FfmsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FfmsApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        return applicationBuilder.sources(FfmsApplication.class);
    }
}
