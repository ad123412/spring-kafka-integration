package com.ada.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;

@Slf4j
@Configuration
public class LibraryEventConsumer {

    @KafkaListener(
            topicPartitions = {@TopicPartition(topic = "library-events", partitions = {"0"})},
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "ada"
    )
    public void listenLibraryEventFromPartition0(ConsumerRecord<?, ?> record) {
        log.info("listening in listenLibraryEventFromPartition0 ...");
        log.info("topic : {}", record.topic());
        log.info("message {}", record.value());
    }

    @KafkaListener(
            topicPartitions = {@TopicPartition(topic = "library-events", partitions = {"1"})},
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "ada"
    )
    public void listenLibraryEventFromPartition1(String message) {
        log.info("listening in listenLibraryEventFromPartition1 ...");
        log.info("message {}", message);
    }
}
