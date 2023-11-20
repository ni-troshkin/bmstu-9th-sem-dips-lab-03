package com.libraryservice.mapper;

import com.libraryservice.dto.LibraryResponse;
import com.libraryservice.entity.Library;
import org.springframework.stereotype.Component;

@Component
public class LibraryMapper {
    /**
     * Конвертация из сущности БД Library в сущность DTO LibraryResponse
     * @param library сущность БД, описывающая библиотеку
     * @return DTO с информацией о библиотеке
     */
    public LibraryResponse toLibraryResponse(Library library) {
        return new LibraryResponse(library.getLibraryUid(), library.getName(),
                library.getAddress(), library.getCity());
    }
}
