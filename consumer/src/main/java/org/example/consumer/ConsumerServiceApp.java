package org.example.consumer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties
@Slf4j
public class ConsumerServiceApp implements CommandLineRunner
{
    public static void main( String[] args )
    {
        SpringApplication.run(ConsumerServiceApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception 
    {
        log.info("TEST SPRING CLOUD CONTRACT");
    }
}
