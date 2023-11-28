package com.gatewayservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gatewayservice.dto.*;
import com.gatewayservice.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@Tag(name = "RESERVATIONS")
@RequestMapping("/reservations")
public class ReservationController {
    /**
     * Сервис, работающий с прокатом книг
     */
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Получение списка книг, взятых в прокат по имени пользователя
     * @param username имя пользователя, информацию о котором нужно получить
     * @return список книг, взятых в прокат
     */
    @Operation(summary = "Получение списка книг, взятых пользователем в прокат")
    @GetMapping()
    public ResponseEntity<ArrayList<BookReservationResponse>> getReservations(@RequestHeader("X-User-Name") String username) {
        return reservationService.getAllReservations(username);
    }

    /**
     * Взять книгу в библиотеке
     * @param username имя читателя
     * @param req информация о запросе
     * @return список книг, взятых в прокат
     */
    @Operation(summary = "Взять книгу в библиотеке")
    @PostMapping()
    public ResponseEntity<?> takeBook(@RequestHeader("X-User-Name") String username,
                                                     @RequestBody TakeBookRequest req) {
        TakeBookResponse reservation = reservationService.takeBook(username, req);

        if (reservation == null)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse("Bonus Service unavailable"));

        return ResponseEntity.status(HttpStatus.OK).body(reservation);
    }

    /**
     * Вернуть книгу в библиотеку
     * @param reservationUid UUID брони, которую закрывает читатель
     * @param username имя читателя
     * @param req информация о возврате
     */
    @Operation(summary = "Вернуть книгу в библиотеку")
    @PostMapping("/{reservationUid}/return")
    public ResponseEntity<String> returnBook(@PathVariable UUID reservationUid,
                                                   @RequestHeader("X-User-Name") String username,
                                                   @RequestBody ReturnBookRequest req) throws JsonProcessingException {
        HttpStatus status = reservationService.returnBook(reservationUid, username, req);

        return ResponseEntity.status(status).build();
    }
}
