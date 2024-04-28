package org.example.producer_kafka.application.features;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.example.items_api_service.CreateItemRequest;
import org.example.items_api_service.IncorrectCreateItemRequestException;
import org.example.items_api_service.ItemCreatedEvent;
import org.example.items_api_service.ItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateItemImpl implements CreateItem
{
    @Value("#{ItemCreatedTopic.name}")
    private String topicName;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public CompletableFuture<ItemDto> run(CreateItemRequest request) throws IncorrectCreateItemRequestException
    {
        ThrowIfRequestIsNotValid(request);
        
        return
            kafkaTemplate.send(
                topicName, 
                ItemCreatedEvent.of(
                    UUID.randomUUID().toString(), 
                    request.getName(), 
                    LocalDateTime.now()
                )
            )
            .whenComplete(this::handleSendResult)
            .thenApply(v -> ((ItemCreatedEvent)v.getProducerRecord().value()).toItemDto());
    }
    
    private void ThrowIfRequestIsNotValid(CreateItemRequest request) 
    {
        if (!StringUtils.hasText(request.getName()))
        {
            throw new IncorrectCreateItemRequestException();
        }
    }   

    private void handleSendResult(SendResult<String, Object> sendResult, Throwable exception)
    {
        if (!Objects.isNull(exception))
        {
            log.error("Exception thown during the event sending:\n{}", exception.getMessage());
            return;
        }

        var record = sendResult.getProducerRecord();

        record.headers().forEach(h -> log.info("HEADER{key=\"{}\", value=\"{}\"}", h.key(), h.value()));
        
        log.info("Timestamp of event sending: {}", record.timestamp());
        log.info("Partition that the event was sent to: {}", record.partition());
        log.info("Key of sent event message: {}", record.key());
        log.info("Content of sent event:\n{}", record.value());
    }
}
