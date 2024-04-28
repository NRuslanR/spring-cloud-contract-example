package org.example.producer_kafka.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("ItemCreatedTopic")
@ConfigurationProperties(prefix = "application.kafka.topics.item-created")
public class ItemCreatedTopicProperties extends KafkaTopicProperties
{
}
