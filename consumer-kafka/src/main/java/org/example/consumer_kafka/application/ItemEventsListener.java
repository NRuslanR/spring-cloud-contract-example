package org.example.consumer_kafka.application;

import java.util.UUID;

import org.example.items_api_service.ItemCreatedEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@KafkaListener(
    topicPartitions = {
        @TopicPartition(
            topic = "${application.kafka.topics.item-created.name}",
            partitions = {
                "0"
            }
        )
    }, 
    groupId = "${spring.kafka.consumer.group-id}"
)
@Slf4j
public class ItemEventsListener 
{
    private final ItemRepository itemRepository;

    @KafkaHandler
    @Transactional
    public void handleItemCreatedEvent(
        @Payload ItemCreatedEvent event,
        @Header(name = KafkaHeaders.RECEIVED_PARTITION) int parition
    )
    {
        log.info("ITEM CREATED EVENT received from partition {}", parition);

        var item = Item.of(UUID.fromString(event.getId()), event.getName(), event.getCreatedAt());

        itemRepository.save(item);
    }
}
