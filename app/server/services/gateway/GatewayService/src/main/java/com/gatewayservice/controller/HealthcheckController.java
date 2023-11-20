package com.gatewayservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "HEALTHCHECK")
@RequestMapping("/manage")
public class HealthcheckController {
    public HealthcheckController() {}

    /**
     * Специальная ручка для проверки готовности сервиса
     * @return статус 200 OK
     */
    @Operation(summary = "Health check")
    @GetMapping("/health")
    public ResponseEntity<String> returnOk() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
