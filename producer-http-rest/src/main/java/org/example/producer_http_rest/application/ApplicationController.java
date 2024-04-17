package org.example.producer_http_rest.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.examples.items_api_service.CreateItemRequest;
import org.examples.items_api_service.IncorrectCreateItemRequestException;
import org.examples.items_api_service.ItemDto;
import org.examples.items_api_service.ItemsApiService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/items")
public class ApplicationController implements ItemsApiService 
{
    @PostMapping 
    @ResponseStatus(code = HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody CreateItemRequest request)
    {
        ensureRequestIsValid(request);

        return new ItemDto(
            UUID.randomUUID().toString(), 
            request.getName(), 
            LocalDateTime.now()
        );
    }

    private void ensureRequestIsValid(CreateItemRequest request) 
    {
        if (!StringUtils.hasText(request.getName()))
        {
            throw new IncorrectCreateItemRequestException(
                "Item's name must not be empty"
            );
        }
    }
}
