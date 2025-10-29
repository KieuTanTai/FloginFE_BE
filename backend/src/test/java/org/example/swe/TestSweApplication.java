package org.example.swe;

import org.springframework.boot.SpringApplication;

public class TestSweApplication {

    public static void main(String[] args) {
        SpringApplication.from(SweApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
