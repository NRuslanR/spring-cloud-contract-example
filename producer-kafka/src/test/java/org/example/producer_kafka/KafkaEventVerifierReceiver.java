package org.example.producer_kafka;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import lombok.SneakyThrows;

public class KafkaEventVerifierReceiver implements MessageVerifierReceiver<Message<?>> 
{
    private Logger log = LoggerFactory.getLogger(KafkaEventVerifierReceiver.class);
    
    private BlockingQueue<Message<?>> messageQueue = new ArrayBlockingQueue<>(1);

    @Override
    public Message<?> receive(String destination, YamlContract contract) 
    {
        return receive(destination, 5, TimeUnit.SECONDS, contract);
    }

    @Override
    @SneakyThrows
    public Message<?> receive(String destination, long timeout, TimeUnit timeUnit, YamlContract contract) 
    {
        var message = messageQueue.poll(timeout, timeUnit);

        if (!Objects.isNull(message))
        {
            log.info("Message was received from destination [{}]", destination);
        }
        
        return message;
    }

    @KafkaListener(
        topics = {
            "${application.kafka.topics.item-created.name}"
        },
        groupId = "${spring.kafka.consumer.group-id}"
    )
    @SneakyThrows
    public void handleItemCreatedEvent(Message<?> message)
    {
        messageQueue.put(message);
    }
}
