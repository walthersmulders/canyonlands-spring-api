package com.walthersmulders.service;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.exception.GenericBadRequestException;
import com.walthersmulders.mapstruct.dto.bookgenre.BookGenre;
import com.walthersmulders.mapstruct.dto.bookgenre.BookGenreUpsert;
import com.walthersmulders.mapstruct.mapper.BookGenreMapper;
import com.walthersmulders.persistance.entity.BookGenreEntity;
import com.walthersmulders.persistance.repository.BookGenreRepository;
import com.walthersmulders.persistance.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Map.entry;

@Service
@Slf4j
public class BookGenreService {
    private static final String BOOK_GENRE = "Book Genre";

    private final BookGenreRepository bookGenreRepository;
    private final BookGenreMapper     bookGenreMapper;
    private final BookRepository      bookRepository;

    public BookGenreService(
            BookGenreRepository bookGenreRepository,
            BookGenreMapper bookGenreMapper,
            BookRepository bookRepository
    ) {
        this.bookGenreRepository = bookGenreRepository;
        this.bookGenreMapper = bookGenreMapper;
        this.bookRepository = bookRepository;
    }

    public List<BookGenre> getGenres() {
        log.info("Getting all genres");

        List<BookGenreEntity> bookGenres = bookGenreRepository.findAll();

        log.info("Found {} genres", bookGenres.size());

        return bookGenres.isEmpty() ? Collections.emptyList()
                                    : bookGenres.stream()
                                                .map(bookGenreMapper::entityToBookGenre)
                                                .toList();
    }

    public BookGenre create(BookGenreUpsert bookGenreUpsert) {
        log.info("Creating genre");

        boolean exists = bookGenreRepository.exists(bookGenreUpsert.genre(), bookGenreUpsert.subGenre());

        if (exists) {
            log.error(
                    "Genre with genre {} and sub genre {} already exists",
                    bookGenreUpsert.genre(),
                    bookGenreUpsert.subGenre()
            );

            throw new EntityExistsException(
                    BOOK_GENRE, Map.ofEntries(
                    entry("genre", bookGenreUpsert.genre()),
                    entry("subGenre", bookGenreUpsert.subGenre())
            ));
        }

        BookGenreEntity bookGenre = bookGenreMapper.bookGenreUpsertToEntity(bookGenreUpsert);

        bookGenreRepository.save(bookGenre);

        log.info("Created genre with id: {}", bookGenre.getBookGenreID());

        return bookGenreMapper.entityToBookGenre(bookGenre);
    }

    public BookGenre getGenre(UUID id) {
        log.info("Getting genre with id: {}", id);

        return bookGenreMapper.entityToBookGenre(
                bookGenreRepository.findById(id)
                                   .orElseThrow(() -> new EntityNotFoundException(
                                           BOOK_GENRE, Map.of("bookGenreId", id.toString()))
                                   )
        );
    }

    public void update(UUID id, BookGenreUpsert bookGenreUpsert) {
        log.info("Updating genre with id: {}", id);

        Optional<BookGenreEntity> existingBookGenre = bookGenreRepository.findById(id);

        if (existingBookGenre.isEmpty()) {
            log.error("Genre with id {} not found", id);

            throw new EntityNotFoundException(BOOK_GENRE, Map.of("bookGenreId", id.toString()));
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingBookGenre.get().checkUpdateDtoEqualsEntity(bookGenreUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            BookGenreEntity updatedBookGenre = bookGenreMapper.bookGenreEntityUpdateMerge(
                    existingBookGenre.get(),
                    bookGenreUpsert
            );

            bookGenreRepository.save(updatedBookGenre);

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

        bookGenreRepository.deleteById(id);

        log.info("Deleted genre with id: {}", id);
    }
}
