package com.libraryservice.mapper;

import com.libraryservice.dto.BookInfo;
import com.libraryservice.dto.LibraryBookResponse;
import com.libraryservice.dto.LibraryResponse;
import com.libraryservice.entity.Book;
import com.libraryservice.entity.Library;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {
    /**
     * Конвертация из сущности БД Book в сущность DTO BookResponse
     * @param book сущность БД, описывающая книгу
     * @return DTO с информацией о книге
     */
    public LibraryBookResponse toLibraryBookResponse(Book book) {
        return new LibraryBookResponse(book.getBookUid(), book.getName(),
                book.getAuthor(), book.getGenre(),
                book.getCondition().toString(), book.getAvailableCnt());
    }

    public BookInfo toBookInfo(Book book) {
        System.out.println(book.getBookUid().toString() + " " + book.getName() + " "
                + book.getAuthor() + " " + book.getGenre());
        return new BookInfo(book.getBookUid().toString(), book.getName(),
                book.getAuthor(), book.getGenre());
    }
}
