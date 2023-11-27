package com.gatewayservice.service;

import com.gatewayservice.dto.LibraryBookResponse;
import com.gatewayservice.dto.LibraryResponse;
import com.gatewayservice.dto.UserRatingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class LibraryService {
    private final RestTemplate restTemplate;
    private final String serverUrl;

    private final CircuitBreakerFactory circuitBreakerFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingService.class);
    private final CircuitBreaker libraryCircuitBreaker;
    private final CircuitBreaker bookCircuitBreaker;

    public LibraryService(RestTemplate restTemplate, CircuitBreakerFactory cbf,
                          @Value("${library.server.url}") String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
        this.circuitBreakerFactory = cbf;
        this.libraryCircuitBreaker = circuitBreakerFactory.create("libraryCb");
        this.bookCircuitBreaker = circuitBreakerFactory.create("bookCb");
    }

    public ResponseEntity<ArrayList<LibraryResponse>> fallbackLibraryResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<ArrayList<LibraryBookResponse>> fallbackLibraryBookResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<ArrayList<LibraryResponse>> getLibrariesByCity(String city) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<ArrayList<LibraryResponse>> libs = null;
        try {
            libs = libraryCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            serverUrl + "/api/v1/libraries?city=" + city,
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<ArrayList<LibraryResponse>>() {
                            }
                    ), throwable -> fallbackLibraryResponse()
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return libs;
    }

    public ResponseEntity<ArrayList<LibraryBookResponse>> getBooksByLibrary(UUID libraryUid, boolean showAll) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<ArrayList<LibraryBookResponse>> books = null;
        try {
            books = bookCircuitBreaker.run(
                    () -> restTemplate.exchange(
                        serverUrl + "/api/v1/libraries/" + libraryUid + "/books?showAll=" + Boolean.valueOf(showAll).toString(),
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<ArrayList<LibraryBookResponse>>() {
                        }
                    ), throwable -> fallbackLibraryBookResponse()
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return books;
    }
}
