package com.reservationservice.repository;

import com.reservationservice.entity.Reservation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Интерфейс репозитория используется для работы с таблицей, отвечающей за прокат книг
 */
public interface IReservationRepo {
    /**
     * Получение всех книг, взятых пользователем в прокат
     * @param username имя пользователя, информацию о котором требуется получить
     * @return список книг, когда-либо взятых пользователем в прокат
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public ArrayList<Reservation> getAllReservations(String username) throws SQLException;

    /**
     * Получение количества книг на руках у читателя
     * @param username имя пользователя, информацию о котором требуется получить
     * @return число, равное количеству книг на руках у пользователя
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public int countRented(String username) throws SQLException;

    /**
     * Создание новой записи в базе проката книг
     * @param reservation структура, содержащая информацию о пользователе, книге, библиотеке и прокате
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void createReservation(Reservation reservation) throws SQLException;

    /**
     * Обновление записи в базе проката книг при возврате
     * @param reservationUid UUID брони, информацию о которой требуется обновить
     * @param isExpired флаг, если установлен, книга сдана не в срок, иначе в срок
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void closeReservation(UUID reservationUid, boolean isExpired) throws SQLException;

    /**
     * Удаление записи в базе проката книг при откате операции
     * @param reservationUid UUID брони, информацию о которой требуется удалить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void deleteReservation(UUID reservationUid) throws SQLException;

    /**
     * Получение информации о брони
     * @param reservationUid UUID брони, информацию о которой требуется получить
     * @return информация об указанной брони
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public Reservation getReservation(UUID reservationUid) throws SQLException;
}
