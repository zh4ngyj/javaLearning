package com.example.app;

import com.example.greeting.GreetingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class GreetingApplication
{
    public static void main(String[] args) {
        SpringApplication.run(GreetingApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(GreetingService greetingService) {
        return args -> {
            String message = greetingService.greet("SpringBoot 2.6.7");
            System.out.println("=========================================");
            System.out.println(message);
            System.out.println("=========================================");
        };
    }
}
