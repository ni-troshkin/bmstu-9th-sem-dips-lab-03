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
public class TakeBookRequest {
    UUID bookUid;
    UUID libraryUid;
    String tillDate;
}
