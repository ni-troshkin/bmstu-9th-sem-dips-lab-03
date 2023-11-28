package com.gatewayservice.controller;

import com.gatewayservice.dto.ErrorResponse;
import com.gatewayservice.dto.UserRatingResponse;
import com.gatewayservice.mapper.RatingMapper;
import com.gatewayservice.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "RATING")
@RequestMapping("/rating")
public class RatingController {
    /**
     * Сервис, работающий с пользователями
     */
    private final RatingService ratingService;

    private final RatingMapper mapper;

    public RatingController(RatingService ratingService, RatingMapper mapper) {
        this.ratingService = ratingService;
        this.mapper = mapper;
    }

    /**
     * Получение рейтинга пользователя по его имени
     * @param username имя пользователя, информацию о котором требуется получить
     * @return сущность с рейтингом пользователя и статус 200 OK
     * @throws Exception при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить рейтинг пользователя")
    @GetMapping()
    public ResponseEntity<?> getPersonById(@RequestHeader("X-User-Name") String username) throws Exception {

        ResponseEntity<UserRatingResponse> response = ratingService.getUserRating(username);
        if (response.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse("Bonus Service unavailable"));

        return response;
    }
}
