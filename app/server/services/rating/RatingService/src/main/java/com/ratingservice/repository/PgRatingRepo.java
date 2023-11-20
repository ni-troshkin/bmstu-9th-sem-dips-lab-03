package com.ratingservice.repository;

import com.ratingservice.entity.Rating;
import com.ratingservice.utils.ConnectionManager;
import com.ratingservice.utils.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

/**
 * Репозиторий используется для работы с таблицей places базы данных PostgreSQL
 * Для подключения используется драйвер JDBC
 */
@Repository
public class PgRatingRepo implements IRatingRepo {
    /**
     * Объект подключения к БД
     */
    private final Connection conn = ConnectionManager.open();

    /**
     * Получение рейтинга пользователя по его имени
     * @param username имя пользователя, информацию о котором требуется получить
     * @return число от 0 до 100, равное рейтингу пользователя
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public int getRatingByUsername(String username) throws SQLException {
        String getRating = "SELECT stars FROM public.rating " +
                "WHERE username = ?";

        PreparedStatement getRatingQuery = conn.prepareStatement(getRating);
        getRatingQuery.setString(1, username);
        ResultSet rs = getRatingQuery.executeQuery();

        if (rs.next())
            return rs.getInt("stars");

        return 0;
    }

    /**
     * Изменение рейтинга пользователя
     * @param username имя пользователя, информацию о котором требуется обновить
     * @param newRating новое значение рейтинга (на сколько изменился)
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public void updateRating(String username, int newRating) throws SQLException {
        String ratingUpd = "UPDATE public.rating SET stars = ? " +
                "WHERE username = ?";

        PreparedStatement ratingUpdate = conn.prepareStatement(ratingUpd);
        ratingUpdate.setInt(1, newRating);
        ratingUpdate.setString(2, username);
        ratingUpdate.executeUpdate();
    }

    /**
     * Добавление нового пользователя
     * @param username имя пользователя, информацию о котором требуется обновить
     * @param newRating значение рейтинга
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void addUser(String username, int newRating) throws SQLException {
        String ratingUpd = "INSERT INTO public.rating (username, stars) " +
                "VALUES (?, ?)";

        PreparedStatement ratingUpdate = conn.prepareStatement(ratingUpd);
        ratingUpdate.setString(1, username);
        ratingUpdate.setInt(2, newRating);
        ratingUpdate.executeUpdate();
    }
}
