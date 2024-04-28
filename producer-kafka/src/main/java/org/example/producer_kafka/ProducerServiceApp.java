package org.example.producer_kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ProducerServiceApp 
{
    public static void main( String[] args )
    {
        SpringApplication.run(ProducerServiceApp.class);
    }
}
