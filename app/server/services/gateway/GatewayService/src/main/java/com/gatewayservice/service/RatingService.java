package com.gatewayservice.service;

import com.gatewayservice.dto.LibraryPaginationResponse;
import com.gatewayservice.dto.UserRatingResponse;
import com.gatewayservice.producer.RatingProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class RatingService {
    private final RestTemplate restTemplate;

//    private final RatingProducer producer;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingService.class);
    private final CircuitBreaker circuitBreaker;
    private final String serverUrl;

    public RatingService(RestTemplate restTemplate, CircuitBreakerFactory cbf,
                         @Value("${rating.server.url}") String serverUrl
//                         RatingProducer producer
                         ) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = cbf;
        this.serverUrl = serverUrl;
        this.circuitBreaker = circuitBreakerFactory.create("userRatingCb");
//        this.producer = producer;
    }

    public ResponseEntity<UserRatingResponse> fallbackUserRatingResponse() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new UserRatingResponse(0));
    }

    public ResponseEntity<UserRatingResponse> getUserRating(String username) {
//        if (fallbackCnt > MAX_FALLBACK_CNT)
//            return fallbackUserRatingResponse().getBody();
        LOGGER.debug("Обработка рейтинга {}", username);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<UserRatingResponse> rating = null;
        try {
            rating = circuitBreaker.run(
                    () -> restTemplate.exchange(
                            serverUrl + "/api/v1/rating",
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<UserRatingResponse>() {
                            }
                    ), throwable -> fallbackUserRatingResponse()
            );

        /*;*/
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return rating;
    }
}
