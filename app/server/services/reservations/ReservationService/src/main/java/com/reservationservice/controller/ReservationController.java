package com.reservationservice.controller;

import com.reservationservice.dto.ReservationRequest;
import com.reservationservice.dto.ReservationResponse;
import com.reservationservice.entity.Reservation;
import com.reservationservice.mapper.ReservationMapper;
import com.reservationservice.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@Tag(name = "RESERVATIONS")
@RequestMapping("/reservations")
public class ReservationController {
    /**
     * Сервис, работающий с пользователями
     */
    private final ReservationService reservationService;

    private final ReservationMapper mapper;

    public ReservationController(ReservationService reservationService, ReservationMapper mapper) {
        this.reservationService = reservationService;
        this.mapper = mapper;
    }

    /**
     * Получение списка книг, взятых в прокат по имени пользователя
     * @param username имя пользователя, информацию о котором нужно получить
     * @return список книг, взятых в прокат
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получение списка книг, взятых пользователем в прокат")
    @GetMapping()
    public ResponseEntity<ArrayList<ReservationResponse>> getReservations(@RequestHeader("X-User-Name") String username) throws SQLException {
        ArrayList<Reservation> reservations = reservationService.getAllReservations(username);

        ArrayList<ReservationResponse> allRes = new ArrayList<>();
        for (Reservation res : reservations) {
            allRes.add(mapper.toReservationResponse(res));
        }

        return ResponseEntity.status(HttpStatus.OK).body(allRes);
    }

    /**
     * Получение количества книг на руках у пользователя
     * @param username имя пользователя, информацию о котором нужно получить
     * @return количество книг, взятых в прокат
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получение количества книг на руках у пользователя")
    @GetMapping("/rented")
    public ResponseEntity<Integer> countReservations(@RequestHeader("X-User-Name") String username) throws SQLException {
        int reservationsCnt = reservationService.countRented(username);
        return ResponseEntity.status(HttpStatus.OK).body(Integer.valueOf(reservationsCnt));
    }

    /**
     * Создание новой брони
     * @param reqReservation объект с информацией, которую клиент отправил при создании пользователя\
     * @param username имя пользователя, берущего книгу в прокат
     * @return информация о новой брони книги
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     * @throws URISyntaxException при неуспешном создании URI нового пользователя
     */
    @Operation(summary = "Взять книгу в библиотеке")
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestHeader("X-User-Name") String username,
                                                    @RequestBody ReservationRequest reqReservation) throws URISyntaxException, SQLException {
        Reservation reservation = mapper.fromReservationRequest(reqReservation, username);
        reservationService.createReservation(reservation);

        return ResponseEntity.status(HttpStatus.OK).body(mapper.toReservationResponse(reservation));
    }

    /**
     * Вернуть книгу в библиотеку
     * @param reservationUid UUID брони, которую закрывает читатель
     * @param isExpired просрочена книга или нет
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Вернуть книгу в библиотеку")
    @PostMapping("/{reservationUid}/return")
    public ResponseEntity<String> closeReservation(@PathVariable UUID reservationUid,
                                                                @RequestParam boolean isExpired) throws SQLException {
        reservationService.closeReservation(reservationUid, isExpired);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Получить информацию о брони
     * @param reservationUid UUID брони, о которой нужна информация
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить информацию о брони")
    @GetMapping("/{reservationUid}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable UUID reservationUid) throws SQLException {
        Reservation reservation = reservationService.getReservation(reservationUid);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toReservationResponse(reservation));
    }

    /**
     * Отменить бронь
     * @param reservationUid UUID брони, которую нужно удалить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Удалить бронь")
    @DeleteMapping("/{reservationUid}")
    public ResponseEntity<String> deleteReservation(@PathVariable UUID reservationUid) throws SQLException {
        reservationService.deleteReservation(reservationUid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
