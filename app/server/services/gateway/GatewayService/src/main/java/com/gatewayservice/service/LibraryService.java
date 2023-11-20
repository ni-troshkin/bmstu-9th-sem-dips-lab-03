package com.gatewayservice.service;

import com.gatewayservice.dto.LibraryBookResponse;
import com.gatewayservice.dto.LibraryResponse;
import com.gatewayservice.dto.UserRatingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class LibraryService {
    private final RestTemplate restTemplate;
    private final String serverUrl;

    public LibraryService(RestTemplate restTemplate, @Value("${library.server.url}") String serverUrl) {
        this.restTemplate = restTemplate;
        this.serverUrl = serverUrl;
    }

    public ArrayList<LibraryResponse> getLibrariesByCity(String city) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<ArrayList<LibraryResponse>> libs = null;
        try {
            libs = restTemplate.exchange(
                    serverUrl + "/api/v1/libraries?city=" + city,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ArrayList<LibraryResponse>>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return libs.getBody();
    }

    public ArrayList<LibraryBookResponse> getBooksByLibrary(UUID libraryUid, boolean showAll) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<ArrayList<LibraryBookResponse>> books = null;
        try {
            books = restTemplate.exchange(
                    serverUrl + "/api/v1/libraries/" + libraryUid + "/books?showAll=" + Boolean.valueOf(showAll).toString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ArrayList<LibraryBookResponse>>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return books.getBody();
    }
}
