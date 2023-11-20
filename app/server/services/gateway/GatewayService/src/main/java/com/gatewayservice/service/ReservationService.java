package com.gatewayservice.service;

import com.gatewayservice.dto.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ReservationService {
    private final RestTemplate restTemplate;
    private final String ratingServerUrl;
    private final String libServerUrl;
    private final String reservServerUrl;

    public ReservationService(RestTemplate restTemplate, @Value("${library.server.url}") String libServerUrl,
                              @Value("${rating.server.url}") String ratingServerUrl,
                              @Value("${reservations.server.url}") String reservServerUrl) {
        this.restTemplate = restTemplate;
        this.libServerUrl = libServerUrl;
        this.ratingServerUrl = ratingServerUrl;
        this.reservServerUrl = reservServerUrl;
    }

    private BookInfo getBookInfo(UUID bookUid) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<BookInfo> book = null;

        try {
            book = restTemplate.exchange(
                    libServerUrl + "/api/v1/books/" + bookUid.toString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<BookInfo>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return book.getBody();
    }

    private LibraryResponse getLibraryInfo(UUID libraryUid) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<LibraryResponse> lib = null;

        try {
            lib = restTemplate.exchange(
                    libServerUrl + "/api/v1/libraries/" + libraryUid.toString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<LibraryResponse>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return lib.getBody();
    }

    private ReservationResponse getReservationInfo(UUID reservationUid) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<ReservationResponse> res = null;

        try {
            res = restTemplate.exchange(
                    reservServerUrl + "/api/v1/reservations/" + reservationUid.toString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ReservationResponse>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return res.getBody();
    }

    private LibraryBookResponse getLibraryBookInfo(UUID libraryUid, UUID bookUid) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<LibraryBookResponse> book = null;

        try {
            book = restTemplate.exchange(
                    libServerUrl + "/api/v1/libraries/" + libraryUid.toString()
                            + "/books/" + bookUid.toString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<LibraryBookResponse>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return book.getBody();
    }

    private int countRented(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<Integer> rented = null;
        try {
            rented = restTemplate.exchange(
                    reservServerUrl + "/api/v1/reservations/rented",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Integer>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return rented.getBody();
    }

    private UserRatingResponse getRating(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<UserRatingResponse> rating = null;
        try {
            rating = restTemplate.exchange(
                    ratingServerUrl + "/api/v1/rating",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<UserRatingResponse>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return rating.getBody();
    }

    private void updateRating(String username, int delta) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        try {
            restTemplate.exchange(
                    ratingServerUrl + "/api/v1/rating/?delta=" + Integer.valueOf(delta).toString(),
                    HttpMethod.PUT,
                    entity,
                    void.class
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    private ReservationResponse createReservation(String username, TakeBookRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<TakeBookRequest> entity = new HttpEntity<>(req, headers);
        ResponseEntity<ReservationResponse> reservation = null;
        try {
            reservation = restTemplate.exchange(
                    reservServerUrl + "/api/v1/reservations",
                    HttpMethod.POST,
                    entity,
                    ReservationResponse.class);

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return reservation.getBody();
    }

    private void closeReservation(UUID reservationUid, boolean isExpired) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        try {
            restTemplate.exchange(
                    reservServerUrl + "/api/v1/reservations/" + reservationUid.toString()
                            + "/return?isExpired=" + Boolean.valueOf(isExpired).toString(),
                    HttpMethod.PUT,
                    entity,
                    void.class);

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    private void updAvailable(TakeBookRequest req, boolean isRented) {
        HttpEntity<String> entity = new HttpEntity<>("body");

        try {
            restTemplate.exchange(
                    libServerUrl + "/api/v1/libraries/" + req.getLibraryUid().toString()
                            + "/books/" + req.getBookUid().toString() + "/?rent=" + Boolean.valueOf(isRented).toString(),
                    HttpMethod.POST,
                    entity,
                    void.class);

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    private void addUser(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        try {
            restTemplate.exchange(
                    ratingServerUrl + "/api/v1/rating",
                    HttpMethod.POST,
                    entity,
                    void.class
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BookReservationResponse> getAllReservations(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<ArrayList<ReservationResponse>> reservations = null;
        try {
            reservations = restTemplate.exchange(
                    reservServerUrl + "/api/v1/reservations",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ArrayList<ReservationResponse>>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        ArrayList<BookReservationResponse> allRes = new ArrayList<>();
        for (ReservationResponse res : reservations.getBody()) {
            BookInfo book = getBookInfo(res.getBookUid());
            LibraryResponse lib = getLibraryInfo(res.getLibraryUid());

            allRes.add(new BookReservationResponse(res.getReservationUid(),
                    res.getStatus(), res.getStartDate(), res.getTillDate(), book, lib));
        }

        return allRes;
    }

    public TakeBookResponse takeBook(String username, TakeBookRequest req) {
        int rented = countRented(username);
        UserRatingResponse rating = getRating(username);
        if (rating.getStars() == 0) {
            rating.setStars(1);
        }
        if (rented >= rating.getStars())
            System.out.println("Много");
        ReservationResponse reservation = createReservation(username, req);
        updAvailable(req, true);
        return new TakeBookResponse(reservation.getReservationUid(), reservation.getStatus(),
                reservation.getStartDate(), reservation.getTillDate(), getBookInfo(reservation.getBookUid()),
                getLibraryInfo(reservation.getLibraryUid()), rating);
    }

    public void returnBook(UUID reservationUid, String username, ReturnBookRequest req) {
        ReservationResponse reservation = getReservationInfo(reservationUid);
        boolean expired = LocalDate.parse(reservation.getTillDate(), DateTimeFormatter.ISO_DATE).isBefore(
                LocalDate.parse(req.getDate(), DateTimeFormatter.ISO_DATE));

        updAvailable(new TakeBookRequest(reservation.getBookUid(), reservation.getLibraryUid(),
                reservation.getTillDate()), false);

        closeReservation(reservationUid, expired);

        if (expired)
            updateRating(username, -10);

        LibraryBookResponse bookInfo = getLibraryBookInfo(reservation.getLibraryUid(),
                                                            reservation.getBookUid());

        if (!bookInfo.getCondition().equals(req.getCondition()))
            updateRating(username, -10);
        else if (!expired)
            updateRating(username, 1);

    }
}
