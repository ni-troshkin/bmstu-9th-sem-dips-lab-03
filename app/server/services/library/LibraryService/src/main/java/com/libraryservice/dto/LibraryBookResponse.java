package com.libraryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryBookResponse {
    UUID bookUid;
    String name;
    String author;
    String genre;
    String condition;
    int availableCount;
}
