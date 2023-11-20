package com.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TakeBookResponse {
    UUID reservationUid;
    String status;
    String startDate;
    String tillDate;
    BookInfo book;
    LibraryResponse library;
    UserRatingResponse rating;
}
