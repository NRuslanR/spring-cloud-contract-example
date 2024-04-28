package org.example.producer_kafka.application.features;

import java.util.concurrent.CompletableFuture;

import org.example.items_api_service.CreateItemRequest;
import org.example.items_api_service.IncorrectCreateItemRequestException;
import org.example.items_api_service.ItemDto;

public interface CreateItem 
{
    CompletableFuture<ItemDto> run(CreateItemRequest request) throws IncorrectCreateItemRequestException;
}
