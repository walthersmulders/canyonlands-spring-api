package com.walthersmulders.service;

import com.walthersmulders.mapstruct.dto.GenreBook;
import com.walthersmulders.mapstruct.dto.GenreBookNoID;
import com.walthersmulders.mapstruct.mapper.GenreBookMapper;
import com.walthersmulders.persistance.entity.GenreBookEntity;
import com.walthersmulders.persistance.repository.GenreBookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class GenreBookService {
    private final GenreBookRepository genreBookRepository;
    private final GenreBookMapper     genreBookMapper;

    public GenreBookService(GenreBookRepository genreBookRepository, GenreBookMapper genreBookMapper) {
        this.genreBookRepository = genreBookRepository;
        this.genreBookMapper = genreBookMapper;
    }

    public List<GenreBook> getGenres() {
        log.info("Getting all genres");

        List<GenreBookEntity> bookGenres = genreBookRepository.findAll();

        log.info("Found {} genres", bookGenres.size());

        return bookGenres.isEmpty() ? List.of()
                                    : bookGenres.stream()
                                                .map(genreBookMapper::entityToGenreBook)
                                                .toList();
    }

    public GenreBook create(GenreBookNoID genreBookNoID) {
        log.info("Creating genre");

        boolean exists = genreBookRepository.exists(genreBookNoID.genre(), genreBookNoID.subGenre());

        if (exists) {
            log.error("Genre with genre {} and sub genre {} already exists", genreBookNoID.genre(), genreBookNoID.subGenre());
            // TODO :: custom exception
            return null;
        }

        GenreBookEntity bookGenre = genreBookMapper.genreBookNoIDToEntity(genreBookNoID);

        genreBookRepository.save(bookGenre);

        log.info("Created genre with id: {}", bookGenre.getBookGenreID());

        return genreBookMapper.entityToGenreBook(bookGenre);
    }

    public GenreBook getGenre(UUID id) {
        log.info("Getting genre with id: {}", id);

        // TODO :: new EntityNotFoundException("BookGenre", Map.of("bookGenreId", String.valueOf(bookGenreId)))));

        return genreBookMapper.entityToGenreBook(
                genreBookRepository.findById(id)
                                   .orElseThrow(() -> new EntityNotFoundException("Not Found"))
        );
    }

    public void update(UUID id, GenreBookNoID genreBookNoID) {
        log.info("Updating genre with id: {}", id);

        Optional<GenreBookEntity> existingBookGenre = genreBookRepository.findById(id);

        if (existingBookGenre.isEmpty()) {
            log.error("Genre with id {} not found", id);
            // TODO :: custom entity not found exception
            return;
        }

        GenreBookEntity updatedBookGenre = genreBookMapper.genreBookEntityUpdateMerge(
                existingBookGenre.get(),
                genreBookNoID
        );

        genreBookRepository.save(updatedBookGenre);

        log.info("Updated genre with id: {}", id);
    }

    public void delete(UUID id) {
        log.info("Deleting genre with id: {}", id);

        genreBookRepository.deleteById(id);

        log.info("Deleted genre with id: {}", id);
    }
}
