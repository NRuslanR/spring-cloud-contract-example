package org.example.items_api_service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ItemsApiService 
{
    @PostMapping
    public ItemDto createItem(@RequestBody CreateItemRequest request)
        throws IncorrectCreateItemRequestException;
} 
