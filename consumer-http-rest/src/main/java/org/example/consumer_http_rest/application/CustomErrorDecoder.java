package org.example.consumer_http_rest.application;

import org.apache.coyote.BadRequestException;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.NotFoundException;

public class CustomErrorDecoder implements ErrorDecoder 
{
    @Override
    public Exception decode(String methodKey, Response response) 
    {
        switch (response.status())
        {
            case 400:
                return new BadRequestException();

            case 404:
                return new NotFoundException();

            default:
                return new Exception("Generic error");
        }
    }
}
