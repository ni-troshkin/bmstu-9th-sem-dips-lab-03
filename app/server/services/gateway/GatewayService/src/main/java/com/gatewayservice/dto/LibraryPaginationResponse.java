package com.gatewayservice.dto;

import com.gatewayservice.service.LibraryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryPaginationResponse {
    int page;
    int pageSize;
    int totalElements;
    ArrayList<LibraryResponse> items;
}
