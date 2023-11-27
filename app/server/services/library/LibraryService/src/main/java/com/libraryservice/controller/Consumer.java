package com.libraryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.libraryservice.dto.ReturnBookRequest;
import com.libraryservice.exception.BookIsNotAvailable;
import com.libraryservice.service.LibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.UUID;

@Slf4j
@Component
public class Consumer {
    private static final String libraryTopic = "${topic.name}";

    private final ObjectMapper objectMapper;
    private final LibraryService libraryService;

//    private final RatingMapper mapper;

    @Autowired
    public Consumer(ObjectMapper objectMapper, LibraryService libraryService) {
        this.objectMapper = objectMapper;
        this.libraryService = libraryService;
    }

    @KafkaListener(topics = libraryTopic)
    public ResponseEntity<String> consumeReturnBook(String message) throws SQLException, JsonProcessingException {
        log.info("message consumed {}", message);
        ReturnBookRequest req = objectMapper.readValue(message, ReturnBookRequest.class);

        libraryService.returnBook(UUID.fromString(req.getLibraryUid()),
                                  UUID.fromString(req.getBookUid()));

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
