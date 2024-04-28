package org.example.producer_kafka.application.config;

import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public abstract class KafkaTopicProperties 
{
    private String name;
    private int partitionCount;
    private short replicationFactor;
}
