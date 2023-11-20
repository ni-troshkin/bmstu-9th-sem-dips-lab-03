package com.reservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    UUID bookUid;
    UUID libraryUid;
    String tillDate;
}
