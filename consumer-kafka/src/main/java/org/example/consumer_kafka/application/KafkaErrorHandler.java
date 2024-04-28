package org.example.consumer_kafka.application;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

public class KafkaErrorHandler implements CommonErrorHandler 
{

    @Override
    public boolean handleOne(
        Exception thrownException, 
        ConsumerRecord<?, ?> record, 
        Consumer<?, ?> consumer,
        MessageListenerContainer container
    ) 
    {
        return handleException(thrownException, consumer);
    }

    @Override
    public void handleOtherException(
        Exception thrownException, 
        Consumer<?, ?> consumer,
        MessageListenerContainer container, 
        boolean batchListener
    ) 
    {
        handleException(thrownException, consumer);
    }
    
    private boolean handleException(Exception thrownException, Consumer<?, ?> consumer) 
    {
        logger().getLog().error("Exception thrown:\n{}", thrownException);

        if (!(thrownException instanceof RecordDeserializationException ex))
            return true;

        consumer.seek(ex.topicPartition(), ex.offset() + 1);
        consumer.commitSync();

        return true;
    }
}
