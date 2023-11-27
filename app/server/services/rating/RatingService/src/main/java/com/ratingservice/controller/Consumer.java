package com.ratingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratingservice.dto.UpdateRatingRequest;
import com.ratingservice.dto.UserRatingResponse;
import com.ratingservice.mapper.RatingMapper;
import com.ratingservice.service.RatingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Slf4j
@Component
public class Consumer {
    private static final String ratingTopic = "${topic.name}";

    private final ObjectMapper objectMapper;
    private final RatingService ratingService;

    private final RatingMapper mapper;

    @Autowired
    public Consumer(ObjectMapper objectMapper, RatingService ratingService,
                    RatingMapper mapper) {
        this.objectMapper = objectMapper;
        this.ratingService = ratingService;
        this.mapper = mapper;
    }

    @KafkaListener(topics = ratingTopic)
    public ResponseEntity<String> consumeRatingUpdate(String message) throws SQLException, JsonProcessingException {
        log.info("message consumed {}", message);
        UpdateRatingRequest req = objectMapper.readValue(message, UpdateRatingRequest.class);

        ratingService.updateRating(req.getUsername(), req.getDelta());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
