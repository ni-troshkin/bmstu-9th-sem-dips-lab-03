package com.libraryservice.controller;

import com.libraryservice.dto.BookInfo;
import com.libraryservice.dto.LibraryBookResponse;
import com.libraryservice.dto.LibraryResponse;
import com.libraryservice.entity.Book;
import com.libraryservice.entity.Library;
import com.libraryservice.exception.BookIsNotAvailable;
import com.libraryservice.mapper.BookMapper;
import com.libraryservice.mapper.LibraryMapper;
import com.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@Tag(name = "LIBRARIES")
public class LibraryController {
    /**
     * Сервис, работающий с пользователями
     */
    private final LibraryService libraryService;

    private final BookMapper bookMapper;
    private final LibraryMapper libraryMapper;

    public LibraryController(LibraryService libraryService, BookMapper bookMapper,
                             LibraryMapper libraryMapper) {
        this.libraryService = libraryService;
        this.bookMapper = bookMapper;
        this.libraryMapper = libraryMapper;
    }

    /**
     * Получение списка библиотек по городу
     * @param city город, в котором ищем библиотеки
     * @return список библиотек в указанном городе
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить список библиотек в городе")
    @GetMapping("/libraries")
    public ResponseEntity<ArrayList<LibraryResponse>> getLibrariesByCity(@RequestParam("city") String city) throws SQLException {
        ArrayList<Library> libraries = libraryService.getLibrariesByCity(city);

        ArrayList<LibraryResponse> allLibs = new ArrayList<>();
        for (Library lib : libraries) {
            allLibs.add(libraryMapper.toLibraryResponse(lib));
        }

        return ResponseEntity.status(HttpStatus.OK).body(allLibs);
    }

    /**
     * Получение списка книг в выбранной библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить список книг
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить список книг в библиотеке")
    @GetMapping("/libraries/{libraryUid}/books")
    public ResponseEntity<ArrayList<LibraryBookResponse>> updateRating(@PathVariable UUID libraryUid) throws SQLException {
        ArrayList<Book> books = libraryService.getBooksByLibrary(libraryUid);

        ArrayList<LibraryBookResponse> allBooks = new ArrayList<>();
        for (Book b : books) {
            allBooks.add(bookMapper.toLibraryBookResponse(b));
        }

        return ResponseEntity.status(HttpStatus.OK).body(allBooks);
    }

    /**
     * Взятие и возврат книги в библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить книгу
     * @param bookUid UUID книги, которую хотим получить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Взять или вернуть книгу")
    @PutMapping("/libraries/{libraryUid}/books/{bookUid}")
    public ResponseEntity<String> bookOperation(@PathVariable UUID libraryUid, @PathVariable UUID bookUid,
                                                @RequestParam("rent") boolean rent) throws SQLException {
        if (!rent)
            libraryService.returnBook(libraryUid, bookUid);

        else {
            try {
                libraryService.takeBook(libraryUid, bookUid);
            } catch (BookIsNotAvailable e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Получение информации о книге в конкретной библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить книгу
     * @param bookUid UUID книги, которую хотим получить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить информацию о книге в конкретной библиотеке")
    @GetMapping("/libraries/{libraryUid}/books/{bookUid}")
    public ResponseEntity<LibraryBookResponse> getLibraryBookInfo(@PathVariable UUID libraryUid,
                                                                  @PathVariable UUID bookUid) throws SQLException {
        Book book = libraryService.getLibraryBookInfo(libraryUid, bookUid);
        return ResponseEntity.status(HttpStatus.OK).body(bookMapper.toLibraryBookResponse(book));
    }

    /**
     * Получение информации о библиотеке
     * @param libraryUid UUID библиотеки, о которой нужна информация
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить информацию о библиотеке")
    @GetMapping("/libraries/{libraryUid}")
    public ResponseEntity<LibraryResponse> getLibraryInfo(@PathVariable UUID libraryUid) throws SQLException {
        Library lib = libraryService.getLibraryInfo(libraryUid);
        return ResponseEntity.status(HttpStatus.OK).body(libraryMapper.toLibraryResponse(lib));
    }
}
