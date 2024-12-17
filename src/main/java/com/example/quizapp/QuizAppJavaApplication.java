package com.example.quizapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class QuizAppJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizAppJavaApplication.class, args);
    }

}
