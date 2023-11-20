package com.reservationservice.mapper;

import com.reservationservice.dto.ReservationRequest;
import com.reservationservice.dto.ReservationResponse;
import com.reservationservice.entity.Reservation;
import com.reservationservice.entity.Status;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class ReservationMapper {
    /**
     * Конвертация из сущности БД Reservation в сущность DTO ReservationResponse
     * @param reservation сущность БД, описывающая прокат книги
     * @return DTO с информацией о прокате
     */
    public ReservationResponse toReservationResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getReservationUid(), reservation.getUsername(),
                reservation.getBookUid(), reservation.getLibraryUid(), reservation.getStatus().toString(),
                reservation.getStartDate().format(DateTimeFormatter.ISO_DATE),
                reservation.getTillDate().format(DateTimeFormatter.ISO_DATE));
    }

    public Reservation fromReservationRequest(ReservationRequest req, String username) {
        return new Reservation(0, UUID.randomUUID(), username, req.getBookUid(), req.getLibraryUid(),
                Status.RENTED, LocalDate.now(ZoneId.of("UTC")), LocalDate.parse(req.getTillDate(), DateTimeFormatter.ISO_DATE));
    }
}
