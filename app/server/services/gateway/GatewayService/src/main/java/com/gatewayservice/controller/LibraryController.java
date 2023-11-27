package com.gatewayservice.controller;

import com.gatewayservice.dto.LibraryBookPaginationResponse;
import com.gatewayservice.dto.LibraryBookResponse;
import com.gatewayservice.dto.LibraryPaginationResponse;
import com.gatewayservice.dto.LibraryResponse;
import com.gatewayservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@Tag(name = "LIBRARIES")
@RequestMapping("/libraries")
public class LibraryController {
    /**
     * Контроллер, работающий с библиотеками
     */
    private final LibraryService libraryService;


    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * Получение списка библиотек по городу
     * @param city город, в котором ищем библиотеки
     * @param page параметр пагинации
     * @param size параметр пагинации
     * @return список библиотек в указанном городе
     */
    @Operation(summary = "Получить список библиотек в городе")
    @GetMapping()
    public ResponseEntity<LibraryPaginationResponse> getLibrariesByCity(@RequestParam(value = "city", required = true) String city,
                                                                        @RequestParam(value = "page", required = false) Integer page,
                                                                        @RequestParam(value = "size", required = false) Integer size) {
        ResponseEntity<ArrayList<LibraryResponse>> response = libraryService.getLibrariesByCity(city);
        ArrayList<LibraryResponse> allLibs = response.getBody();
        HttpStatus status = response.getStatusCode();

        if (status != HttpStatus.OK)
            return ResponseEntity.status(status).build();

        if (page == null)
            return ResponseEntity.status(status).body(new LibraryPaginationResponse(1, 1, allLibs.size(), allLibs));

        int maxPage = allLibs.size() / size + 1;

        if (page > maxPage)
            page = maxPage;

        ArrayList<LibraryResponse> pageLibs =
                new ArrayList<>(allLibs.subList((page - 1) * size, Integer.min(page * size, allLibs.size())));

        return ResponseEntity.status(status).body(new LibraryPaginationResponse(page, size, allLibs.size(), pageLibs));
    }

    /**
     * Получение списка книг в выбранной библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить список книг
     * @param page параметр пагинации
     * @param size параметр пагинации
     * @param showAll true - показывать недоступные для аренды книги
     */
    @Operation(summary = "Получить список книг в библиотеке")
    @GetMapping("/{libraryUid}/books")
    public ResponseEntity<LibraryBookPaginationResponse> updateRating(@PathVariable UUID libraryUid,
                                                                      @RequestParam(value = "page", required = false) Integer page,
                                                                      @RequestParam(value = "size", required = false) Integer size,
                                                                      @RequestParam(value = "showAll", required = false, defaultValue = "true") boolean showAll)  {
        ResponseEntity<ArrayList<LibraryBookResponse>> response = libraryService.getBooksByLibrary(libraryUid, showAll);
        ArrayList<LibraryBookResponse> books = response.getBody();
        HttpStatus status = response.getStatusCode();

        if (status != HttpStatus.OK)
            return ResponseEntity.status(status).build();

        if (page == null)
            return ResponseEntity.status(status).body(new LibraryBookPaginationResponse(1, 1, books.size(), books));

        int maxPage = books.size() / size + 1;

        if (page > maxPage)
            page = maxPage;

        ArrayList<LibraryBookResponse> pageLibs =
                new ArrayList<>(books.subList((page - 1) * size, Integer.min(page * size, books.size())));

        return ResponseEntity.status(status).body(new LibraryBookPaginationResponse(page, size, books.size(), pageLibs));
    }
}
