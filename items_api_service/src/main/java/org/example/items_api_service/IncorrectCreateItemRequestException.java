package org.example.items_api_service;

public class IncorrectCreateItemRequestException extends RuntimeException 
{
    public IncorrectCreateItemRequestException()
    {
        super("Incorrect request to create item");
    }

    public IncorrectCreateItemRequestException(String message) 
    {
        super(message);
    }
}
