package com.gatewayservice.service;

import com.gatewayservice.dto.UserRatingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RatingService {
    private final RestTemplate restTemplate;
    private final String serverUrl;

    public RatingService(RestTemplate restTemplate, @Value("${rating.server.url}") String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public UserRatingResponse getUserRating(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<UserRatingResponse> rating = null;
        try {
            rating = restTemplate.exchange(
                    serverUrl + "/api/v1/rating",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<UserRatingResponse>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return rating.getBody();
    }
}
