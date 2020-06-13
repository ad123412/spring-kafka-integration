package com.ada.kafka.controller;

import com.ada.kafka.domain.LibraryEvent;
import com.ada.kafka.domain.LibraryEventType;
import com.ada.kafka.producer.approach1.LibraryEventProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LibraryController {

    private LibraryEventProducer libraryEventProducer;

    @PostMapping(value = {"/v1/libraryEvent"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException {
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        log.info("before sending library event ....");
        libraryEventProducer.sendToLibraryEvent(libraryEvent);
        log.info("after sending library event ....");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @Autowired
    public void setLibraryEventProducer(LibraryEventProducer libraryEventProducer) {
        this.libraryEventProducer = libraryEventProducer;
    }
}
