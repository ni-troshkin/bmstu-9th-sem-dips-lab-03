package com.ratingservice.repository;

import com.ratingservice.entity.Rating;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Интерфейс репозитория используется для работы с таблицей, отвечающей за места в базе данных
 */
public interface IRatingRepo {
    /**
     * Получение рейтинга пользователя по его имени
     * @param username имя пользователя, информацию о котором требуется получить
     * @return число от 0 до 100, равное рейтингу пользователя
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public int getRatingByUsername(String username) throws SQLException;

    /**
     * Изменение рейтинга пользователя
     * @param username имя пользователя, информацию о котором требуется обновить
     * @param newRating новое значение рейтинга (на сколько изменился)
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void updateRating(String username, int newRating) throws SQLException;

    /**
     * Добавление нового пользователя
     * @param username имя пользователя, информацию о котором требуется обновить
     * @param newRating значение рейтинга
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void addUser(String username, int newRating) throws SQLException;
}
