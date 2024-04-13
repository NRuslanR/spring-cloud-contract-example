package org.example.consumer.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import java.util.stream.Stream;

import org.examples.items_api_service.CreateItemRequest;
import org.examples.items_api_service.ItemDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(
    webEnvironment = WebEnvironment.MOCK,
    properties = {
        "producer.stubs.port=8081"
    }
)
@AutoConfigureStubRunner(
    stubsMode = StubsMode.LOCAL,
    ids = "org.example:producer:+:stubs:8081"
)
@TestInstance(Lifecycle.PER_CLASS)
@Slf4j
public class ProducerServiceClientContractTests 
{ 
    private final String producerStubsBasePath;
    private final String producerStubsItemsPath;

    public ProducerServiceClientContractTests(
        @Value("${producer.stubs.port}") String producerStubsPort
    )
    {
        producerStubsBasePath = "http://localhost:" + producerStubsPort;
        producerStubsItemsPath = producerStubsBasePath + "/api/items";
    }

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void should_Return_CreatedItem_When_Request_Is_Correct()
    {
        var request = new CreateItemRequest("Item#1");

        var itemEntity =
            restTemplate.postForEntity(
                producerStubsItemsPath, 
                request,
                ItemDto.class
            );

        assertEquals(HttpStatus.CREATED, itemEntity.getStatusCode());

        var item = itemEntity.getBody();

        assertNotNull(item);
        assertTrue(StringUtils.hasText(item.getId()));
        assertEquals(request.getName(), item.getName());
        assertNotNull(item.getCreatedAt());
    }

    @Test
    public void should_RaiseError_When_CreateItemRequest_Is_InCorrect()
    {
        var request = new CreateItemRequest("");

        try
        {
            restTemplate.postForEntity(
                producerStubsItemsPath, 
                request, 
                ItemDto.class
            );
        }

        catch (HttpClientErrorException exception)
        {
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

            assertTrue(
                StringUtils.hasText(
                    JsonPath
                        .parse(exception.getResponseBodyAsString())
                        .read("$.error.message"
                    )
                )
            );
        }
    }
}
