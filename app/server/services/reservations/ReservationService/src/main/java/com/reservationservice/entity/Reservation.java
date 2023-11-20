package com.reservationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class Reservation {
    int id;
    UUID reservationUid;
    String username;
    UUID bookUid;
    UUID libraryUid;
    Status status;
    LocalDate startDate;
    LocalDate tillDate;
}
