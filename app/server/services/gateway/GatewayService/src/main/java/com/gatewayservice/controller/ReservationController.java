package com.gatewayservice.controller;

import com.gatewayservice.dto.BookReservationResponse;
import com.gatewayservice.dto.ReturnBookRequest;
import com.gatewayservice.dto.TakeBookRequest;
import com.gatewayservice.dto.TakeBookResponse;
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
        ArrayList<BookReservationResponse> reservations = reservationService.getAllReservations(username);

        return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }

    /**
     * Получение списка книг, взятых в прокат по имени пользователя
     * @param username имя пользователя, информацию о котором нужно получить
     * @return список книг, взятых в прокат
     */
    @Operation(summary = "Получение списка книг, взятых пользователем в прокат")
    @PostMapping()
    public ResponseEntity<TakeBookResponse> takeBook(@RequestHeader("X-User-Name") String username,
                                                     @RequestBody TakeBookRequest req) {
        TakeBookResponse reservation = reservationService.takeBook(username, req);

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
                                                   @RequestBody ReturnBookRequest req) {
        reservationService.returnBook(reservationUid, username, req);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
