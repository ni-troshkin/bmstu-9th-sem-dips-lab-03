package com.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryBookPaginationResponse {
    int page;
    int pageSize;
    int totalElements;
    ArrayList<LibraryBookResponse> items;
}
