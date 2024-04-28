package org.example.consumer_kafka.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NonNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaConsumerProperties 
{
    private String bootstrapServers;
    private String groupId;
}
