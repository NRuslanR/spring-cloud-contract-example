package org.example.consumer_http_rest.application;

import org.example.items_api_service.ItemsApiService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
    name = "${producer.api.items-path}",
    configuration = {
        FeignConfig.class
    },
    fallback = ProducerServiceClientFallback.class
)
public interface ProducerServiceClient extends ItemsApiService
{
 
}
