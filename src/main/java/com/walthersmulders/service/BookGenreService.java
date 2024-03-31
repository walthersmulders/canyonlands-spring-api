package com.walthersmulders.service;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.bookgenre.BookGenre;
import com.walthersmulders.mapstruct.dto.bookgenre.BookGenreNoID;
import com.walthersmulders.mapstruct.mapper.BookGenreMapper;
import com.walthersmulders.persistance.entity.BookGenreEntity;
import com.walthersmulders.persistance.repository.BookGenreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Map.entry;

@Service
@Slf4j
public class BookGenreService {
    private static final String BOOK_GENRE = "Book Genre";

    private final BookGenreRepository bookGenreRepository;
    private final BookGenreMapper     bookGenreMapper;

    public BookGenreService(BookGenreRepository bookGenreRepository, BookGenreMapper bookGenreMapper) {
        this.bookGenreRepository = bookGenreRepository;
        this.bookGenreMapper = bookGenreMapper;
    }

    public List<BookGenre> getGenres() {
        log.info("Getting all genres");

        List<BookGenreEntity> bookGenres = bookGenreRepository.findAll();

        log.info("Found {} genres", bookGenres.size());

        return bookGenres.isEmpty() ? List.of()
                                    : bookGenres.stream()
                                                .map(bookGenreMapper::entityToBookGenre)
                                                .toList();
    }

    public BookGenre create(BookGenreNoID bookGenreNoID) {
        log.info("Creating genre");

        boolean exists = bookGenreRepository.exists(bookGenreNoID.genre(), bookGenreNoID.subGenre());

        if (exists) {
            log.error(
                    "Genre with genre {} and sub genre {} already exists",
                    bookGenreNoID.genre(),
                    bookGenreNoID.subGenre()
            );

            throw new EntityExistsException(
                    BOOK_GENRE, Map.ofEntries(
                    entry("genre", bookGenreNoID.genre()),
                    entry("subGenre", bookGenreNoID.subGenre())
            ));
        }

        BookGenreEntity bookGenre = bookGenreMapper.bookGenreNoIDToEntity(bookGenreNoID);

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

    public void update(UUID id, BookGenreNoID bookGenreNoID) {
        log.info("Updating genre with id: {}", id);

        Optional<BookGenreEntity> existingBookGenre = bookGenreRepository.findById(id);

        if (existingBookGenre.isEmpty()) {
            log.error("Genre with id {} not found", id);

            throw new EntityNotFoundException(BOOK_GENRE, Map.of("bookGenreId", id.toString()));
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingBookGenre.get().checkUpdateDtoEqualsEntity(bookGenreNoID)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            BookGenreEntity updatedBookGenre = bookGenreMapper.bookGenreEntityUpdateMerge(
                    existingBookGenre.get(),
                    bookGenreNoID
            );

            bookGenreRepository.save(updatedBookGenre);

            log.info("Updated genre with id: {}", id);
        }
    }

    public void delete(UUID id) {
        log.info("Deleting genre with id: {}", id);

        bookGenreRepository.deleteById(id);

        log.info("Deleted genre with id: {}", id);
    }
}
