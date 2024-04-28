package org.example.producer_kafka.application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.example.items_api_service.CreateItemRequest;
import org.example.items_api_service.IncorrectCreateItemRequestException;
import org.example.items_api_service.ItemCreatedEvent;
import org.example.items_api_service.ItemDto;
import org.example.producer_kafka.application.features.CreateItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(path = "/api/items")
@RequiredArgsConstructor
@Slf4j
public class ApplicationController 
{
    private final CreateItem createItem;

    @Value("#{ItemCreatedTopic.name}")
    private String topicName;

    @PostMapping
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void createItem(@RequestBody @NonNull CreateItemRequest request)
    {
        createItem
            .run(request)
            .whenComplete(this::handleCreateItem);
    }

    private void handleCreateItem(ItemDto itemDto, Throwable exception)
    {
        if (!Objects.isNull(exception))
        {
            log.error("Exception thown during the item creation:\n{}", exception.getMessage());
            return;
        }

        log.info("The next item was created:\n{}", itemDto);
    }
}
