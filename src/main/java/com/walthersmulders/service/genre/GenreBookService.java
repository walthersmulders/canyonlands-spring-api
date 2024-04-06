package com.walthersmulders.service.genre;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.exception.GenericBadRequestException;
import com.walthersmulders.mapstruct.dto.genre.book.GenreBook;
import com.walthersmulders.mapstruct.dto.genre.book.GenreBookUpsert;
import com.walthersmulders.mapstruct.mapper.GenreBookMapper;
import com.walthersmulders.persistence.entity.genre.GenreBookEntity;
import com.walthersmulders.persistence.repository.book.BookRepository;
import com.walthersmulders.persistence.repository.genre.GenreBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Map.entry;

@Service
@Slf4j
public class GenreBookService {
    private static final String BOOK_GENRE = "Book Genre";

    private final GenreBookRepository genreBookRepository;
    private final GenreBookMapper     genreBookMapper;
    private final BookRepository      bookRepository;

    public GenreBookService(
            GenreBookRepository genreBookRepository,
            GenreBookMapper genreBookMapper,
            BookRepository bookRepository
    ) {
        this.genreBookRepository = genreBookRepository;
        this.genreBookMapper = genreBookMapper;
        this.bookRepository = bookRepository;
    }

    public List<GenreBook> getGenres() {
        log.info("Getting all genres");

        List<GenreBookEntity> bookGenres = genreBookRepository.findAll();

        log.info("Found {} genres", bookGenres.size());

        return bookGenres.isEmpty() ? Collections.emptyList()
                                    : bookGenres.stream()
                                                .map(genreBookMapper::entityToGenreBook)
                                                .toList();
    }

    public GenreBook create(GenreBookUpsert genreBookUpsert) {
        log.info("Creating genre");

        boolean exists = genreBookRepository.exists(genreBookUpsert.genre(), genreBookUpsert.subGenre());

        if (exists) {
            log.error(
                    "Genre with genre {} and sub genre {} already exists",
                    genreBookUpsert.genre(),
                    genreBookUpsert.subGenre()
            );

            throw new EntityExistsException(
                    BOOK_GENRE, Map.ofEntries(
                    entry("genre", genreBookUpsert.genre()),
                    entry("subGenre", genreBookUpsert.subGenre())
            ));
        }

        GenreBookEntity bookGenre = genreBookMapper.genreBookUpsertToEntity(genreBookUpsert);

        genreBookRepository.save(bookGenre);

        log.info("Created genre with id: {}", bookGenre.getBookGenreID());

        return genreBookMapper.entityToGenreBook(bookGenre);
    }

    public GenreBook getGenre(UUID id) {
        log.info("Getting genre with id: {}", id);

        return genreBookMapper.entityToGenreBook(
                genreBookRepository.findById(id)
                                   .orElseThrow(() -> new EntityNotFoundException(
                                           BOOK_GENRE, Map.of("bookGenreId", id.toString()))
                                   )
        );
    }

    public void update(UUID id, GenreBookUpsert genreBookUpsert) {
        log.info("Updating genre with id: {}", id);

        Optional<GenreBookEntity> existingBookGenre = genreBookRepository.findById(id);

        if (existingBookGenre.isEmpty()) {
            log.error("Genre with id {} not found", id);

            throw new EntityNotFoundException(BOOK_GENRE, Map.of("bookGenreId", id.toString()));
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingBookGenre.get().checkUpdateDtoEqualsEntity(genreBookUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            GenreBookEntity updatedBookGenre = genreBookMapper.genreBookEntityUpdateMerge(
                    existingBookGenre.get(),
                    genreBookUpsert
            );

            genreBookRepository.save(updatedBookGenre);

            log.info("Updated genre with id: {}", id);
        }
    }

    public void delete(UUID id) {
        log.info("Checking if genre belongs to any book");
        boolean isLinkedToBook = bookRepository.existsByBookGenreID(id);

        if (isLinkedToBook) {
            log.error("Genre with id {} is linked to a book, cannot delete", id);

            throw new GenericBadRequestException(
                    "Genre with id " + id + " is linked to a book, cannot delete"
            );
        }

        log.info("Deleting genre with id: {}", id);

        genreBookRepository.deleteById(id);

        log.info("Deleted genre with id: {}", id);
    }
}
