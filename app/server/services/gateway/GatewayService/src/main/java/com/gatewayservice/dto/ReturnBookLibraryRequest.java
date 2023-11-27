package com.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ReturnBookLibraryRequest {
    String bookUid;
    String libraryUid;
}
