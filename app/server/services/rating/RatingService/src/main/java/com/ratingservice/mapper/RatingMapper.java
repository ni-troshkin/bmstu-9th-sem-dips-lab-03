package com.ratingservice.mapper;

import com.ratingservice.dto.UserRatingResponse;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {
    /**
     * Конвертация из численного рейтинга в сущность DTO UserRatingResponse
     * @param rating рейтинг пользователя
     * @return DTO с рейтингом пользователя
     */
    public UserRatingResponse toRatingResponse(int rating) {
        return new UserRatingResponse(rating);
    }
}
