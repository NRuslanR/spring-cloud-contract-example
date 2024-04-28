package org.example.producer_kafka;

import java.util.UUID;

import org.example.items_api_service.CreateItemRequest;
import org.example.producer_kafka.application.features.CreateItem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(
    webEnvironment = WebEnvironment.NONE,
    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
    },
    classes = {
        ProducerServiceContractTestsConfig.class
    }
)
@Testcontainers(
    disabledWithoutDocker = true,
    parallel = true
)
@AutoConfigureMessageVerifier
@TestInstance(Lifecycle.PER_CLASS)
public class ProducerServiceContractTestsSpec
{
    @Container
    private static KafkaContainer kafkaContainer = 
        new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka")
        );

    @DynamicPropertySource
    public static void resolveKafkaProperties(DynamicPropertyRegistry registry)
    {
        kafkaContainer.start();
    
        registry.add(
            "spring.kafka.bootstrap-servers", 
            kafkaContainer::getBootstrapServers
        );

        registry.add(
            "spring.kafka.producer.bootstrap-servers", 
            kafkaContainer::getBootstrapServers
        );

        registry.add(
            "spring.kafka.consumer.bootstrap-servers", 
            kafkaContainer::getBootstrapServers
        );
    }

    @Autowired
    private CreateItem createItem;

    @AfterAll
    public static void cleanup()
    {
        kafkaContainer.stop();
    }

    public void createItem()
    {
        createItem.run(CreateItemRequest.of(UUID.randomUUID().toString()));
    }
}
