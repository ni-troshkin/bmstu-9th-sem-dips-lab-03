package com.ratingservice.controller;

import com.ratingservice.dto.UserRatingResponse;
import com.ratingservice.mapper.RatingMapper;
import com.ratingservice.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

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
    @Operation(summary = "Get place by id")
    @GetMapping()
    public ResponseEntity<UserRatingResponse> getPersonById(@RequestHeader("X-User-Name") String username) throws Exception {
        int rating = ratingService.getRatingByUsername(username);

        return ResponseEntity.status(HttpStatus.OK).body(mapper.toRatingResponse(rating));
    }

    /**
     * Изменение рейтинга пользователя
     * @param username имя пользователя, информацию о котором требуется обновить
     * @param delta численное изменение рейтинга (на сколько изменился)
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Обновить рейтинг пользователя")
    @PutMapping("/")
    public ResponseEntity<String> updateRating(@RequestHeader("X-User-Name") String username, @RequestParam int delta) throws SQLException {
        ratingService.updateRating(username, delta);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Добавление нового пользователя
     * @param username имя пользователя, информацию о котором требуется обновить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Добавить нового пользователя")
    @PostMapping()
    public ResponseEntity<String> addUser(@RequestHeader("X-User-Name") String username) throws SQLException {
        ratingService.addUser(username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
