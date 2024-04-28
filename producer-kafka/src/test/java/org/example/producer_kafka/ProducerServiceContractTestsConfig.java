package org.example.producer_kafka;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

@TestConfiguration
public class ProducerServiceContractTestsConfig 
{
    @Bean
    public MessageVerifierReceiver<Message<?>> messageVerifierReceiver()   
    {
        return new KafkaEventVerifierReceiver();
    }
}
