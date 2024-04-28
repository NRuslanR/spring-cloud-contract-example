package org.example.consumer_kafka.application;

import java.util.UUID;

import org.example.items_api_service.ItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> 
{

}
