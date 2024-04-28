package org.example.consumer_kafka.application.config;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.consumer_kafka.application.KafkaErrorHandler;
import org.example.items_api_service.ItemCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig 
{
    @NonNull
    private final KafkaConsumerProperties properties;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory()
    {
        var config = 
            Map.<String, Object>of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers(),
                ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
            );

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @SuppressWarnings("rawtypes")
    @Bean
    public KafkaListenerContainerFactory kafkaListenerContainerFactory()
    {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();

        factory.setCommonErrorHandler(commonErrorHandler());
        factory.setRecordMessageConverter(multiTypeConverter());
        factory.setConsumerFactory(consumerFactory()); 
        
        return factory;
    }

    @Bean
    public CommonErrorHandler commonErrorHandler()
    {
        return new KafkaErrorHandler();
    }

    @Bean
    public RecordMessageConverter multiTypeConverter()
    {
        var typeMapper = new DefaultJackson2JavaTypeMapper();

        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages("org.example.items_api_service");

        Map<String, Class<?>> mappings = 
            Map.of(
                ItemCreatedEvent.class.getSimpleName(), 
                ItemCreatedEvent.class
            );

        typeMapper.setIdClassMapping(mappings);

        var converter = new StringJsonMessageConverter();

        converter.setTypeMapper(typeMapper);

        return converter;
    }
}
