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
public class LibraryResponse {
    UUID libraryUid;
    String name;
    String address;
    String city;

    public LibraryResponse(String libraryUid) {
        this.libraryUid = UUID.fromString(libraryUid);
    }
}