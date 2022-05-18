package com.preemptivebookcafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
public class PreemptiveBookcafeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PreemptiveBookcafeApplication.class, args);
    }

}
