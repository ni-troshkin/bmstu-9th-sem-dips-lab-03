package com.gatewayservice.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gatewayservice.dto.ReturnBookLibraryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LibraryProducer {
    @Value("${topic.library.name}")
    private String libraryTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public LibraryProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public String sendMessage(String bookUid, String libraryUid) throws JsonProcessingException {
        ReturnBookLibraryRequest req = new ReturnBookLibraryRequest(bookUid, libraryUid);
        String msg = objectMapper.writeValueAsString(req);

        kafkaTemplate.send(libraryTopic, msg);

        log.info("library update produced {}", msg);

        return "message sent";
    }
}
