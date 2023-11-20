package com.ratingservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Rating {
    int id;
    String username;
    int stars;
}
