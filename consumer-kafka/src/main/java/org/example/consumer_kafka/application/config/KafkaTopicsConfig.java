package org.example.consumer_kafka.application.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicsConfig 
{
    @Bean
    public NewTopic itemCreatedTopic(ItemCreatedTopicProperties properties)
    {
        return 
            TopicBuilder
                .name(properties.getName())
                .partitions(properties.getPartitionCount())
                .replicas(properties.getReplicationFactor())
                .build();
    }
}
