package com.reservationservice.service;

import com.reservationservice.entity.Reservation;
import com.reservationservice.repository.IReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Класс в слое сервисов, обращающийся к репозиторию рейтингов
 */
@Service
public class ReservationService {
    /**
     * Репозиторий, работающий с рейтингами
     */
    @Autowired
    private final IReservationRepo repo;

    public ReservationService(IReservationRepo repo) {
        this.repo = repo;
    }

    /**
     * Получение всех книг, взятых пользователем в прокат
     * @param username имя пользователя, информацию о котором требуется получить
     * @return список книг, когда-либо взятых пользователем в прокат
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public ArrayList<Reservation> getAllReservations(String username) throws SQLException {
        return repo.getAllReservations(username);
    }

    /**
     * Получение информации о брони
     * @param reservationUid UUID брони, информацию о которой требуется получить
     * @return информация об указанной брони
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public Reservation getReservation(UUID reservationUid) throws SQLException {
        return repo.getReservation(reservationUid);
    }

    /**
     * Получение количества книг на руках у читателя
     * @param username имя пользователя, информацию о котором требуется получить
     * @return число, равное количеству книг на руках у пользователя
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public int countRented(String username) throws SQLException {
        return repo.countRented(username);
    }

    /**
     * Создание новой записи в базе проката книг
     * @param reservation структура, содержащая информацию о пользователе, книге, библиотеке и прокате
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void createReservation(Reservation reservation) throws SQLException {
        repo.createReservation(reservation);
    }

    /**
     * Обновление записи в базе проката книг при возврате
     * @param reservationUid UUID брони, информацию о которой требуется обновить
     * @param isExpired флаг, если установлен, книга сдана не в срок, иначе в срок
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void closeReservation(UUID reservationUid, boolean isExpired) throws SQLException {
        repo.closeReservation(reservationUid, isExpired);
    }

    /**
     * Удалить информацию о брони
     * @param reservationUid UUID брони, информацию о которой требуется удалить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void deleteReservation(UUID reservationUid) throws SQLException {
        repo.deleteReservation(reservationUid);
    }
}
