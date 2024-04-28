package org.example.consumer_kafka;

import org.example.consumer_kafka.application.ItemRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.StringUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import lombok.RequiredArgsConstructor;

import static org.awaitility.Awaitility.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
    webEnvironment = WebEnvironment.NONE,
    classes = {
        ConsumerServiceContractTestsConfig.class
    }
)
@TestPropertySource(locations = "classpath:application.test.properties")
@AutoConfigureStubRunner(stubsMode = StubsMode.LOCAL, ids = "org.example:producer_kafka:0.0.1-SNAPSHOT:stubs:8888")
@TestInstance(Lifecycle.PER_CLASS)
@Testcontainers
public class ConsumerServiceContractTests 
{
    @Autowired
    private StubTrigger stubTrigger;
    
    @Autowired
    private ItemRepository itemRepository;

    @Value("${test.contract.messaging.item-created-trigger.label}")
    private String itemCreatedTriggerLabel;
    
    @Container
    private static KafkaContainer kafkaContainer = 
        new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka")
        );

    @DynamicPropertySource
    private static void resolveKafkaProperties(DynamicPropertyRegistry registry)
    {
        kafkaContainer.start();

        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @BeforeAll
    @AfterAll
    public void setupAndCleanup()
    {
        itemRepository.deleteAll();

        kafkaContainer.stop();
    }

    @Test
    public void should_ItemBeCreated_When_ItemCreatedEventReceived()
    {
        triggerItemCreatedEvent();

        assertItemBeCreated();
    }

    private void triggerItemCreatedEvent() 
    {
        stubTrigger.trigger(itemCreatedTriggerLabel);
    }

    private void assertItemBeCreated() 
    {
        await().untilAsserted(() -> {

            assertEquals(itemRepository.count(), 1);

            var item = itemRepository.findAll().stream().findFirst().get();

            assertNotNull(item.getId());
            assertTrue(StringUtils.hasText(item.getName()));
            assertNotNull(item.getCreatedAt());
        });
    }
}
