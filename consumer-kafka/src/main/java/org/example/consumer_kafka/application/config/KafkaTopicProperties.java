package org.example.consumer_kafka.application.config;

import lombok.Data;

@Data
public abstract class KafkaTopicProperties 
{
    private String name;
    private int partitionCount;
    private int replicationFactor;
}
