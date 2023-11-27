package com.gatewayservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gatewayservice.dto.*;
import com.gatewayservice.producer.LibraryProducer;
import com.gatewayservice.producer.RatingProducer;
import org.apache.catalina.User;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.service.Response;

import java.net.UnknownHostException;
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

    private final CircuitBreakerFactory circuitBreakerFactory;
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingService.class);
    private final CircuitBreaker ratingCircuitBreaker;
    private final CircuitBreaker libraryCircuitBreaker;
    private final CircuitBreaker bookCircuitBreaker;
    private final CircuitBreaker libraryBookCircuitBreaker;
    private final CircuitBreaker reservationsCircuitBreaker;

    private final CircuitBreaker rentedCircuitBreaker;
    private final CircuitBreaker reservCircuitBreaker;

    private final RatingProducer ratingProducer;
    private final LibraryProducer libraryProducer;

    public ReservationService(RestTemplate restTemplate, CircuitBreakerFactory cbf,
                              @Value("${library.server.url}") String libServerUrl,
                              @Value("${rating.server.url}") String ratingServerUrl,
                              @Value("${reservations.server.url}") String reservServerUrl,
                              RatingProducer ratingProducer, LibraryProducer libraryProducer) {
        this.restTemplate = restTemplate;
        this.libServerUrl = libServerUrl;
        this.ratingServerUrl = ratingServerUrl;
        this.reservServerUrl = reservServerUrl;
        this.circuitBreakerFactory = cbf;
        this.ratingProducer = ratingProducer;
        this.libraryProducer = libraryProducer;

        this.libraryCircuitBreaker = circuitBreakerFactory.create("reservLibraryCb");
        this.bookCircuitBreaker = circuitBreakerFactory.create("reservBookCb");
        this.libraryBookCircuitBreaker = circuitBreakerFactory.create("reservLibraryBookCb");
        this.reservCircuitBreaker = circuitBreakerFactory.create("reservCb");
        this.reservationsCircuitBreaker = circuitBreakerFactory.create("reservationsCb");
        this.ratingCircuitBreaker = circuitBreakerFactory.create("ratingCb");
        this.rentedCircuitBreaker = circuitBreakerFactory.create("rentedCb");
    }

    private ResponseEntity<BookInfo> fallbackBookInfoResponse(String bookUid) {
        return ResponseEntity.status(HttpStatus.OK).body(new BookInfo(bookUid));
    }

    private ResponseEntity<LibraryResponse> fallbackLibraryResponse(String libraryUid) {
        return ResponseEntity.status(HttpStatus.OK).body(new LibraryResponse(libraryUid));
    }

    private ResponseEntity<ReservationResponse> fallbackReservationResponse(String reservationUid) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private ResponseEntity<LibraryBookResponse> fallbackLibraryBookResponse(String bookUid) {
        LibraryBookResponse response = new LibraryBookResponse(bookUid);
        response.setCondition("EXCELLENT");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private ResponseEntity<Integer> fallbackCountRentedResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<UserRatingResponse> fallbackUserRatingResponse() {
        return ResponseEntity.status(HttpStatus.OK).body(new UserRatingResponse(0));
    }

    public ResponseEntity<ArrayList<ReservationResponse>> fallbackAllReservationsResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private BookInfo getBookInfo(UUID bookUid) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<BookInfo> book = null;

        try {
            book = bookCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            libServerUrl + "/api/v1/books/" + bookUid.toString(),
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<BookInfo>() {
                            }
                    ), throwable -> fallbackBookInfoResponse(bookUid.toString())
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
            lib = libraryCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            libServerUrl + "/api/v1/libraries/" + libraryUid.toString(),
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<LibraryResponse>() {
                            }
                    ), throwable -> fallbackLibraryResponse(libraryUid.toString())
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
            res = reservCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            reservServerUrl + "/api/v1/reservations/" + reservationUid.toString(),
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<ReservationResponse>() {
                            }
                    ), throwable -> fallbackReservationResponse(reservationUid.toString())
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return res.getBody();
    }

    private ResponseEntity<LibraryBookResponse> getLibraryBookInfo(UUID libraryUid, UUID bookUid) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<LibraryBookResponse> book = null;

        try {
            book = libraryBookCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            libServerUrl + "/api/v1/libraries/" + libraryUid.toString()
                                    + "/books/" + bookUid.toString(),
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<LibraryBookResponse>() {
                            }
                    ), throwable -> fallbackLibraryBookResponse(bookUid.toString())
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return book;
    }

    private ResponseEntity<Integer> countRented(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<Integer> rented = null;
        try {
            rented = rentedCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            reservServerUrl + "/api/v1/reservations/rented",
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<Integer>() {
                            }
                    ), throwable -> fallbackCountRentedResponse()
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return rented;
    }

    private ResponseEntity<UserRatingResponse> getRating(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<UserRatingResponse> rating = null;
        try {
            rating = ratingCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            ratingServerUrl + "/api/v1/rating",
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<UserRatingResponse>() {
                            }
                    ), throwable -> fallbackUserRatingResponse()
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return rating;
    }

    private ResponseEntity<String> updateRating(String username, int delta) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(
                    ratingServerUrl + "/api/v1/rating/?delta=" + Integer.valueOf(delta).toString(),
                    HttpMethod.PUT,
                    entity,
                    String.class
            );
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        } catch (ResourceAccessException e) {
            response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return response;
    }

    private ResponseEntity<ReservationResponse> createReservation(String username, TakeBookRequest req) {
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

        return reservation;
    }

    private ResponseEntity<String> closeReservation(UUID reservationUid, boolean isExpired) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<String> response = null;

        try {
            response = restTemplate.exchange(
                    reservServerUrl + "/api/v1/reservations/" + reservationUid.toString()
                            + "/return?isExpired=" + Boolean.valueOf(isExpired).toString(),
                    HttpMethod.POST,
                    entity,
                    String.class);

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return response;
    }

    private ResponseEntity<String> updAvailable(TakeBookRequest req, boolean isRented) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        ResponseEntity<String> response = null;

        try {
            response = restTemplate.exchange(
                    libServerUrl + "/api/v1/libraries/" + req.getLibraryUid().toString()
                            + "/books/" + req.getBookUid().toString() + "/?rent=" + Boolean.valueOf(isRented).toString(),
                    HttpMethod.PUT,
                    entity,
                    String.class);

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        } catch (ResourceAccessException e) {
            response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return response;
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

    public ResponseEntity<ArrayList<BookReservationResponse>> getAllReservations(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Name", username);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<ArrayList<ReservationResponse>> reservations = null;
        try {
            reservations = reservationsCircuitBreaker.run(
                    () -> restTemplate.exchange(
                            reservServerUrl + "/api/v1/reservations",
                            HttpMethod.GET,
                            entity,
                            new ParameterizedTypeReference<ArrayList<ReservationResponse>>() {
                            }
                    ), throwable -> fallbackAllReservationsResponse()
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

        return ResponseEntity.status(reservations.getStatusCode()).body(allRes);
    }

    private void cancelReservation(UUID reservationUid) {
        HttpEntity<String> entity = new HttpEntity<>("body");
        try {
            restTemplate.exchange(
                    reservServerUrl + "/api/v1/reservations/" + reservationUid.toString(),
                    HttpMethod.DELETE,
                    entity,
                    void.class);

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    public TakeBookResponse takeBook(String username, TakeBookRequest req) {
        ResponseEntity<Integer> rentedResponse = countRented(username);
        if (rentedResponse.getStatusCode() != HttpStatus.OK)
            return null;

        int rented = rentedResponse.getBody();

        ResponseEntity<UserRatingResponse> ratingResponse = getRating(username);
        if (ratingResponse.getStatusCode() != HttpStatus.OK)
            return null;

        UserRatingResponse rating = ratingResponse.getBody();

        if (rating.getStars() == 0) {
            rating.setStars(1);
        }
        if (rented >= rating.getStars())
            System.out.println("Много");

        ResponseEntity<ReservationResponse> reservationResponse = createReservation(username, req);

        if (reservationResponse.getStatusCode() != HttpStatus.OK)
            return null;

        ReservationResponse reservation = reservationResponse.getBody();

        ResponseEntity<String> libResponse = updAvailable(req, true);

        if (libResponse == null || libResponse.getStatusCode() != HttpStatus.OK) {
            cancelReservation(reservation.getReservationUid());
            return null;
        }

        return new TakeBookResponse(reservation.getReservationUid(), reservation.getStatus(),
                reservation.getStartDate(), reservation.getTillDate(), getBookInfo(reservation.getBookUid()),
                getLibraryInfo(reservation.getLibraryUid()), rating);
    }

    public HttpStatus returnBook(UUID reservationUid, String username,
                                 ReturnBookRequest req) throws JsonProcessingException {
        ReservationResponse reservation = getReservationInfo(reservationUid);
        boolean expired = LocalDate.parse(reservation.getTillDate(), DateTimeFormatter.ISO_DATE).isBefore(
                LocalDate.parse(req.getDate(), DateTimeFormatter.ISO_DATE));

        ResponseEntity<String> closeResponse = closeReservation(reservationUid, expired);
        if (closeResponse.getStatusCode() != HttpStatus.NO_CONTENT)
            return HttpStatus.INTERNAL_SERVER_ERROR;


        ResponseEntity<String> libResponse = updAvailable(new TakeBookRequest(reservation.getBookUid(),
                reservation.getLibraryUid(), reservation.getTillDate()), false);

        if (libResponse.getStatusCode() != HttpStatus.OK)
            libraryProducer.sendMessage(reservation.getBookUid().toString(),
                    reservation.getLibraryUid().toString());

        int delta = 0;
        if (expired)
            delta -= 10;

        ResponseEntity<LibraryBookResponse> bookInfoResponse = getLibraryBookInfo(reservation.getLibraryUid(),
                                                            reservation.getBookUid());

        LibraryBookResponse bookInfo = bookInfoResponse.getBody();
        if (!bookInfo.getCondition().equals(req.getCondition()))
            delta -= 10;
        else if (!expired)
            delta += 1;

        ResponseEntity<String> ratingResponse = updateRating(username, delta);
        if (ratingResponse.getStatusCode() != HttpStatus.OK)
            ratingProducer.sendMessage(username, delta);

        return HttpStatus.NO_CONTENT;
    }
}
