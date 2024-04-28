package org.example.consumer_kafka.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.kafka.topics.item-created")
public class ItemCreatedTopicProperties extends KafkaTopicProperties 
{
    
}
