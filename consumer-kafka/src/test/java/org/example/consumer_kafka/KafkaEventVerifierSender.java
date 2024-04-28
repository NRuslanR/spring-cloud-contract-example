package org.example.consumer_kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KafkaEventVerifierSender implements MessageVerifierSender<Message<?>>
{
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public <T> void send(T payload, Map<String, Object> headers, String destination, YamlContract contract) 
    {
        Map<String, Object> messageHeaders = !Objects.isNull(headers) ? new HashMap<>(headers) : new HashMap<>();

        messageHeaders.put(KafkaHeaders.TOPIC, destination);

        var message = MessageBuilder.createMessage(payload, new MessageHeaders(messageHeaders));
        
        send(message, destination, contract);
    }

    @Override
    public void send(Message<?> message, String destination, YamlContract contract) 
    {
        kafkaTemplate.send(message);
    }
}
