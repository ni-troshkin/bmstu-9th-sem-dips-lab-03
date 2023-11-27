package com.reservationservice.repository;

import com.reservationservice.entity.Reservation;
import com.reservationservice.entity.Status;
import com.reservationservice.utils.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Интерфейс репозитория используется для работы с таблицей БД PostgreSQL, отвечающей за прокат книг
 * Для подключения используется драйвер JDBC
 */
@Repository
public class PgReservationRepo implements IReservationRepo {
    /**
     * Объект подключения к БД
     */
    private final Connection conn = ConnectionManager.open();

    /**
     * Получение всех книг, взятых пользователем в прокат
     * @param username имя пользователя, информацию о котором требуется получить
     * @return список книг, когда-либо взятых пользователем в прокат
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public ArrayList<Reservation> getAllReservations(String username) throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String getReservations = "SELECT id, reservation_uid, username, book_uid, " +
                "library_uid, status, start_date, till_date " +
                "FROM public.reservation " +
                "WHERE username = ?";
        PreparedStatement reservationsQuery = conn.prepareStatement(getReservations);
        reservationsQuery.setString(1, username);
        ResultSet rs = reservationsQuery.executeQuery();

        while (rs.next())
        {
            Reservation reserve = new Reservation(rs.getInt("id"),
                    rs.getObject("reservation_uid", java.util.UUID.class),
                    rs.getString("username"),
                    rs.getObject("book_uid", java.util.UUID.class),
                    rs.getObject("library_uid", java.util.UUID.class),
                    Status.valueOf(rs.getString("status")),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("till_date").toLocalDate());
            reservations.add(reserve);
        }

        return reservations;
    }

    /**
     * Получение количества книг на руках у читателя
     * @param username имя пользователя, информацию о котором требуется получить
     * @return число, равное количеству книг на руках у пользователя
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public int countRented(String username) throws SQLException {
        String getReservations = "SELECT count(*) AS cnt " +
                "FROM public.reservation " +
                "WHERE username = ? AND status = 'RENTED'";
        PreparedStatement reservationsQuery = conn.prepareStatement(getReservations);
        reservationsQuery.setString(1, username);
        ResultSet rs = reservationsQuery.executeQuery();

        int cntRented = 0;

        if (rs.next())
            cntRented = rs.getInt("cnt");

        return cntRented;
    }

    /**
     * Создание новой записи в базе проката книг
     * @param reservation структура, содержащая информацию о пользователе, книге, библиотеке и прокате
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public void createReservation(Reservation reservation) throws SQLException {
        String reservationAdd = "INSERT INTO public.reservation " +
                "(reservation_uid, username, book_uid, " +
                "library_uid, status, start_date, till_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement reservationInsertion = conn.prepareStatement(reservationAdd);
        reservationInsertion.setObject(1, reservation.getReservationUid());
        reservationInsertion.setString(2, reservation.getUsername());
        reservationInsertion.setObject(3, reservation.getBookUid());
        reservationInsertion.setObject(4, reservation.getLibraryUid());
        reservationInsertion.setString(5, reservation.getStatus().toString());
        reservationInsertion.setDate(6, java.sql.Date.valueOf(reservation.getStartDate()));
        reservationInsertion.setDate(7, java.sql.Date.valueOf(reservation.getTillDate()));
        reservationInsertion.executeUpdate();
    }

    /**
     * Обновление записи в базе проката книг при возврате
     * @param reservationUid UUID брони, информацию о которой требуется обновить
     * @param isExpired флаг, если установлен, книга сдана не в срок, иначе в срок
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Override
    public void closeReservation(UUID reservationUid, boolean isExpired) throws SQLException {
        String newStatus = "";
        if (isExpired)
            newStatus = Status.EXPIRED.toString();
        else
            newStatus = Status.RETURNED.toString();

        String reservationUpd = "UPDATE public.reservation SET status = ? " +
                "WHERE reservation_uid = ?";

        PreparedStatement reservationUpdate = conn.prepareStatement(reservationUpd);
        reservationUpdate.setString(1, newStatus);
        reservationUpdate.setObject(2, reservationUid);
        reservationUpdate.executeUpdate();
    }

    /**
     * Получение информации о брони
     * @param reservationUid UUID брони, информацию о которой требуется получить
     * @return информация об указанной брони
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public Reservation getReservation(UUID reservationUid) throws SQLException {
        Reservation reservation = null;

        String getReservations = "SELECT id, reservation_uid, username, book_uid, " +
                "library_uid, status, start_date, till_date " +
                "FROM public.reservation " +
                "WHERE reservation_uid = ?";
        PreparedStatement reservationsQuery = conn.prepareStatement(getReservations);
        reservationsQuery.setObject(1, reservationUid);
        ResultSet rs = reservationsQuery.executeQuery();

        if (rs.next())
        {
            reservation = new Reservation(rs.getInt("id"),
                    rs.getObject("reservation_uid", java.util.UUID.class),
                    rs.getString("username"),
                    rs.getObject("book_uid", java.util.UUID.class),
                    rs.getObject("library_uid", java.util.UUID.class),
                    Status.valueOf(rs.getString("status")),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("till_date").toLocalDate());
        }

        return reservation;
    }

    /**
     * Удаление записи в базе проката книг при откате операции
     * @param reservationUid UUID брони, информацию о которой требуется удалить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void deleteReservation(UUID reservationUid) throws SQLException {
        String deleteReservation = "DELETE FROM public.reservation " +
                "WHERE reservation_uid = ?";
        PreparedStatement reservationDeletion = conn.prepareStatement(deleteReservation);
        reservationDeletion.setObject(1, reservationUid);
        reservationDeletion.executeUpdate();
    }
}
