package com.libraryservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class BookLibrary {
    UUID libraryUid;
    UUID bookUid;
    int availableCnt;
}
