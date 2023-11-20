package com.libraryservice.controller;

import com.libraryservice.dto.BookInfo;
import com.libraryservice.entity.Book;
import com.libraryservice.mapper.BookMapper;
import com.libraryservice.mapper.LibraryMapper;
import com.libraryservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.UUID;

@RestController
@Tag(name = "BOOKS")
public class BookController {
    private final LibraryService libraryService;

    private final BookMapper bookMapper;

    public BookController(LibraryService libraryService, BookMapper bookMapper) {
        this.libraryService = libraryService;
        this.bookMapper = bookMapper;
    }

    /**
     * Получение информации о книге
     * @param bookUid UUID книги, о которую хотим получить информацию
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    @Operation(summary = "Получить информацию о книге")
    @GetMapping("/books/{bookUid}")
    public ResponseEntity<BookInfo> getBookInfo(@PathVariable UUID bookUid) throws SQLException {
        Book book = libraryService.getBookInfo(bookUid);
        System.out.println(book.toString());
        return ResponseEntity.status(HttpStatus.OK).body(bookMapper.toBookInfo(book));
    }
}
