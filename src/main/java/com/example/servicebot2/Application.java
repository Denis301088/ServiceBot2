package com.example.servicebot2;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync//Подключает возможность работы в фоновом режиме
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
