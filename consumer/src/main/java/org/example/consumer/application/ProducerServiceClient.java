package org.example.consumer.application;

import org.examples.items_api_service.ItemsApiService;
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
