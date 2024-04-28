package org.example.consumer_kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

@TestConfiguration
public class ConsumerServiceContractTestsConfig 
{
    @Bean
    @Autowired
    public MessageVerifierSender<Message<?>> messageVerifierSender(
        KafkaTemplate<String, Object> kafkaTemplate
    )
    {
        return new KafkaEventVerifierSender(kafkaTemplate);
    }
}
