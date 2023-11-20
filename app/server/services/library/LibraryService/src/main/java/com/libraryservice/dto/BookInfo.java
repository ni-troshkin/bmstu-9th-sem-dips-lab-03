package com.libraryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookInfo {
    String bookUid;
    String name;
    String author;
    String genre;
}
