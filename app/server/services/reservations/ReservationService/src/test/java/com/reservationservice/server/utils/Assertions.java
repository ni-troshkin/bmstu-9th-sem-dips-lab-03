package com.reservationservice.server.utils;

import com.reservationservice.entity.Reservation;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void myAssertEqual(Reservation r1, Reservation r2) {
        assertEquals(r1.getId(), r2.getId());
        assertEquals(r1.getBookUid(), r2.getBookUid());
        assertEquals(r1.getReservationUid(), r2.getReservationUid());
        assertEquals(r1.getLibraryUid(), r2.getLibraryUid());
        assertEquals(r1.getUsername(), r2.getUsername());
        assertEquals(r1.getStatus(), r2.getStatus());
        assertEquals(r1.getStartDate(), r2.getStartDate());
        assertEquals(r1.getTillDate(), r2.getTillDate());
    }
}
