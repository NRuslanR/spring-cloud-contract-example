package org.example.consumer_http_rest.application;

import org.examples.items_api_service.CreateItemRequest;
import org.examples.items_api_service.IncorrectCreateItemRequestException;
import org.examples.items_api_service.ItemDto;
import org.springframework.stereotype.Component;

@Component
public class ProducerServiceClientFallback implements ProducerServiceClient 
{
    @Override
    public ItemDto createItem(CreateItemRequest request) 
        throws IncorrectCreateItemRequestException 
    {
        return ItemDto.empty();
    }   
}
