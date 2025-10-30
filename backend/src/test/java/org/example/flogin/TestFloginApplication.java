package org.example.flogin;

import org.springframework.boot.SpringApplication;

public class TestFloginApplication {

    public static void main(String[] args) {
        SpringApplication.from(FloginApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
