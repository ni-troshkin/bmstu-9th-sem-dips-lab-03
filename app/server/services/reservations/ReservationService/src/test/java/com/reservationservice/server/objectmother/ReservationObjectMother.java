package com.reservationservice.server.objectmother;

import com.reservationservice.entity.Reservation;
import com.reservationservice.entity.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class ReservationObjectMother {
    public static Reservation getReservation() {
        return new Reservation(1, UUID.fromString("f682e6de-47da-478a-8571-188fe7904997"),
                "user1", UUID.fromString("ca43068d-f954-4ced-85a1-a8242c8ad517"),
                UUID.fromString("504614b2-55a3-44cb-bd77-c2854511dd62"), Status.RENTED,
                LocalDate.now(), LocalDate.of(2023, 12, 31));
    }

    public static Reservation getAnotherReservation() {
        return new Reservation(2, UUID.fromString("488f84b3-7e24-494a-93ed-98008b64aac9"),
                "user1", UUID.fromString("c3efc2ba-68c3-43fc-8080-9d417431e441"),
                UUID.fromString("2d1bed60-6215-4787-a055-1546a92ff071"), Status.RENTED,
                LocalDate.now(), LocalDate.of(2023, 12, 31));
    }

    public static ArrayList<Reservation> getReservationList() {
        ArrayList<Reservation> lst = new ArrayList<>();
        lst.add(getReservation());
        lst.add(getAnotherReservation());

        return lst;
    }
}
