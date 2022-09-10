package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Demo6Application {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Demo6Application.class);

        builder.headless(false);

        ConfigurableApplicationContext context = builder.run(args);
    }

}
