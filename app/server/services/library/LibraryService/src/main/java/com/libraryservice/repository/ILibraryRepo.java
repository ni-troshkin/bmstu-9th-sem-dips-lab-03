package com.libraryservice.repository;

import com.libraryservice.entity.Book;
import com.libraryservice.entity.Library;
import com.libraryservice.exception.BookIsNotAvailable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Интерфейс репозитория используется для работы с базой данных, отвечающей за библиотеки и книги
 */
public interface ILibraryRepo {
    /**
     * Получение списка библиотек по городу
     * @param city город, в котором ищем библиотеки
     * @return список библиотек в указанном городе
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public ArrayList<Library> getLibrariesByCity(String city) throws SQLException;

    /**
     * Получение списка книг в выбранной библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить список книг
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public ArrayList<Book> getBooksByLibrary(UUID libraryUid) throws SQLException;

    /**
     * Взятие книги в библиотеке (уменьшение поля available_cnt)
     * @param libraryUid UUID библиотеки, в которой хотим получить книгу
     * @param bookUid UUID книги, которую нужно взять в библиотеке
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void takeBook(UUID libraryUid, UUID bookUid) throws SQLException, BookIsNotAvailable;

    /**
     * Возврат книги в библиотеку (увеличение поля available_cnt)
     * @param libraryUid UUID библиотеки, в которую хотим вернуть книгу
     * @param bookUid UUID книги, которую нужно вернуть в библиотеку
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public void returnBook(UUID libraryUid, UUID bookUid) throws SQLException;

    /**
     * Проверка доступности книги в библиотеке
     * @param libraryUid UUID библиотеки, в которой ищем книгу
     * @param bookUid UUID нужной книги
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public boolean isAvailable(UUID libraryUid, UUID bookUid) throws SQLException;

    /**
     * Получение информации о книге в конкретной библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить книгу
     * @param bookUid UUID книги, которую хотим получить
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public Book getLibraryBookInfo(UUID libraryUid, UUID bookUid) throws SQLException;

    /**
     * Получение информации о книге
     * @param bookUid UUID книги, о которую хотим получить информацию
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public Book getBookInfo(UUID bookUid) throws SQLException;

    /**
     * Получение информации о библиотеке
     * @param libraryUid UUID библиотеки, о которой нужна информация
     * @throws SQLException при неуспешном подключении или внутренней ошибке базы данных
     */
    public Library getLibraryInfo(UUID libraryUid) throws SQLException;
}
