package com.ada.kafka.producer.approach1;

import com.ada.kafka.domain.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LibraryEventProducer {

    private KafkaTemplate<Integer, String> kafkaTemplate;
    private ObjectMapper objectMapper;

    public void sendToLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        ListenableFuture<SendResult<Integer, String>> result =
                kafkaTemplate.sendDefault(key, value);
        result.addCallback(successResult -> {
            log.info("successfully sent the event with key {} and value {} to partition {}",
                    key, value, successResult.getRecordMetadata().partition());
        }, throwable -> {
            log.error("error sending the event with key {} and value {}. The error is {}",
                    key, value, throwable.getMessage());
        });
    }

    public void sendToLibraryEvent_Approach2(LibraryEvent libraryEvent) throws JsonProcessingException {
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        ProducerRecord<Integer, String> producerRecord = createProducerRecord(key, value);
        ListenableFuture<SendResult<Integer, String>> result = kafkaTemplate.send(producerRecord);
        result.addCallback(successResult -> {
            log.info("successfully sent the event with key {} and value {} to partition {}",
                    key, value, successResult.getRecordMetadata().partition());
        }, throwable -> {
            log.error("error sending the event with key {} and value {}. The error is {}",
                    key, value, throwable.getMessage());
        });
    }

    private ProducerRecord<Integer, String> createProducerRecord(Integer key, String value) throws JsonProcessingException {
        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<Integer, String>("library-events", null, key, value, headers);
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<Integer, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
