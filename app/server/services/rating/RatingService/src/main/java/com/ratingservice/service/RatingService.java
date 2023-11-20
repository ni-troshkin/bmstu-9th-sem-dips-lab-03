package com.ratingservice.service;

import com.ratingservice.repository.IRatingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Класс в слое сервисов, обращающийся к репозиторию рейтингов
 */
@Service
public class RatingService {
    /**
     * Репозиторий, работающий с рейтингами
     */
    @Autowired
    private final IRatingRepo repo;

    public RatingService(IRatingRepo repo) {
        this.repo = repo;
    }

    /**
     * Получение рейтинга пользователя по его имени
     * @param username имя пользователя, информацию о котором требуется получить
     * @return число от 0 до 100, равное рейтингу пользователя
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public int getRatingByUsername(String username) throws SQLException {
        return repo.getRatingByUsername(username);
    }

    /**
     * Изменение рейтинга пользователя
     * @param username имя пользователя, информацию о котором требуется обновить
     * @param delta численное изменение рейтинга (на сколько изменился)
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void updateRating(String username, int delta) throws SQLException {
        int oldRating = getRatingByUsername(username);
        int newRating = oldRating + delta;

        if (newRating < 1)
            newRating = 1;

        if (newRating > 100)
            newRating = 100;

        if (oldRating == 0)
            repo.addUser(username, newRating);
        else
            repo.updateRating(username, newRating);
    }

    /**
     * Добавление нового пользователя
     * @param username имя пользователя, информацию о котором требуется обновить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void addUser(String username) throws SQLException {
        repo.addUser(username, 1);
    }
}
