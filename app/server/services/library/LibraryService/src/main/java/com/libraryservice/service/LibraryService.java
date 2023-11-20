package com.libraryservice.service;

import com.libraryservice.entity.Book;
import com.libraryservice.entity.Library;
import com.libraryservice.exception.BookIsNotAvailable;
import com.libraryservice.repository.ILibraryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Класс в слое сервисов, обращающийся к репозиторию рейтингов
 */
@Service
public class LibraryService {
    /**
     * Репозиторий, работающий с рейтингами
     */
    @Autowired
    private final ILibraryRepo repo;

    public LibraryService(ILibraryRepo repo) {
        this.repo = repo;
    }

    /**
     * Получение списка библиотек по городу
     * @param city город, в котором ищем библиотеки
     * @return список библиотек в указанном городе
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public ArrayList<Library> getLibrariesByCity(String city) throws SQLException {
       return repo.getLibrariesByCity(city);
    }

    /**
     * Получение списка книг в выбранной библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить список книг
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public ArrayList<Book> getBooksByLibrary(UUID libraryUid) throws SQLException {
        return repo.getBooksByLibrary(libraryUid);
    }

    /**
     * Получение информации о книге в конкретной библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить книгу
     * @param bookUid UUID книги, которую хотим получить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public Book getLibraryBookInfo(UUID libraryUid, UUID bookUid) throws SQLException {
        return repo.getLibraryBookInfo(libraryUid, bookUid);
    }

    /**
     * Взятие книги в библиотеке (уменьшение поля available_cnt)
     * @param libraryUid UUID библиотеки, в которой хотим получить книгу
     * @param bookUid UUID книги, которую нужно взять в библиотеке
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void takeBook(UUID libraryUid, UUID bookUid) throws SQLException, BookIsNotAvailable {
        repo.takeBook(libraryUid, bookUid);
    }

    /**
     * Возврат книги в библиотеку (увеличение поля available_cnt)
     * @param libraryUid UUID библиотеки, в которую хотим вернуть книгу
     * @param bookUid UUID книги, которую нужно вернуть в библиотеку
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void returnBook(UUID libraryUid, UUID bookUid) throws SQLException {
        repo.returnBook(libraryUid, bookUid);
    }

    /**
     * Получение информации о книге
     * @param bookUid UUID книги, о которую хотим получить информацию
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public Book getBookInfo(UUID bookUid) throws SQLException {
        return repo.getBookInfo(bookUid);
    }

    /**
     * Получение информации о библиотеке
     * @param libraryUid UUID библиотеки, о которой нужна информация
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public Library getLibraryInfo(UUID libraryUid) throws SQLException {
        return repo.getLibraryInfo(libraryUid);
    }
}
