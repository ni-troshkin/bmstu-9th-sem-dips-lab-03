package com.reservationservice.dto;

import com.reservationservice.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    UUID reservationUid;
    String username;
    UUID bookUid;
    UUID libraryUid;
    String status;
    String startDate;
    String tillDate;
}
