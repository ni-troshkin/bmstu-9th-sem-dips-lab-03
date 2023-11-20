package com.reservationservice.server.unit;

import com.reservationservice.entity.Reservation;
import com.reservationservice.repository.IReservationRepo;
import com.reservationservice.server.objectmother.ReservationObjectMother;
import com.reservationservice.server.utils.Assertions;
import com.reservationservice.service.ReservationService;
import io.qameta.allure.Description;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {
    private ReservationService service = null;

    private IReservationRepo rep = Mockito.mock(IReservationRepo.class);

    @Before
    public void setUp() {
        service = new ReservationService(rep);
    }

    @Test
    @Description("Тестирование получения брони по ID")
    public void checkGetReservation() throws SQLException {
        // arrange
        Reservation reservation = ReservationObjectMother.getReservation();
        Mockito.when(service.getReservation(UUID.fromString("504614b2-55a3-44cb-bd77-c2854511dd62"))).thenReturn(reservation);

        // act
        Reservation result = service.getReservation(UUID.fromString("504614b2-55a3-44cb-bd77-c2854511dd62"));

        // assert
        Mockito.verify(rep).getReservation(UUID.fromString("504614b2-55a3-44cb-bd77-c2854511dd62"));
        Assertions.myAssertEqual(reservation, result);
    }

    @Test
    @Description("Тестирование получения всех броней пользователя")
    public void checkGetAllReservations() throws SQLException {
        // arrange
        ArrayList<Reservation> lst = ReservationObjectMother.getReservationList();
        Mockito.when(service.getAllReservations("user1")).thenReturn(lst);

        // act
        ArrayList<Reservation> resultLst = service.getAllReservations("user1");

        // assert
        Mockito.verify(rep).getAllReservations("user1");
        assertEquals(lst.size(), resultLst.size());
        for (int i = 0; i < resultLst.size(); i++) {
            Assertions.myAssertEqual(lst.get(i), resultLst.get(i));
        }
    }

    @Test
    @Description("Тестирование добавления брони")
    public void checkCreateReservation() throws SQLException {
        // arrange
        Reservation reservation = ReservationObjectMother.getReservation();

        // act
        service.createReservation(reservation);

        // assert
        Mockito.verify(rep).createReservation(reservation);
    }

    @Test
    @Description("Тестирование закрытия брони")
    public void checkCloseReservation() throws SQLException {
        // act
        service.closeReservation(UUID.fromString("504614b2-55a3-44cb-bd77-c2854511dd62"), true);

        // assert
        Mockito.verify(rep).closeReservation(UUID.fromString("504614b2-55a3-44cb-bd77-c2854511dd62"), true);
    }
}
