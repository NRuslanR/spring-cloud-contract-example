package org.example.consumer.application;

import java.time.LocalDateTime;

import org.examples.items_api_service.CreateItemRequest;
import org.examples.items_api_service.ItemDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/items")
public class ApplicationController 
{
    private final ProducerServiceClient producerServiceClient;

    @PostMapping
    public ExtendedItemDto createItem(@RequestBody CreateItemRequest request)
    {
        return toExtendedItemDto(producerServiceClient.createItem(request));
    }

    private ExtendedItemDto toExtendedItemDto(ItemDto item) 
    {
        return new ExtendedItemDto(
            item.getId(),
            item.getId() + " " + item.getCreatedAt(),
            item.getName(),
            item.getCreatedAt(),
            LocalDateTime.now()
        );
    }

}
