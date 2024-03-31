package com.walthersmulders.service;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.usersbook.UsersBook;
import com.walthersmulders.mapstruct.dto.usersbook.UsersBookUpsert;
import com.walthersmulders.mapstruct.mapper.UserMapper;
import com.walthersmulders.persistance.entity.BookEntity;
import com.walthersmulders.persistance.entity.UserEntity;
import com.walthersmulders.persistance.entity.UsersBookEntity;
import com.walthersmulders.persistance.repository.BookRepository;
import com.walthersmulders.persistance.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UsersBookService {
    private static final String BOOK_ID = "bookID";
    private static final String BOOK    = "Book";

    private final UserRepository userRepository;
    private final UserMapper     userMapper;
    private final BookRepository bookRepository;

    public UsersBookService(
            UserRepository userRepository,
            UserMapper userMapper,
            BookRepository bookRepository
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public UsersBook addBookToUserLibrary(UUID userID, UUID bookID, UsersBookUpsert usersBookUpsert) {
        log.info("Adding book to user library for userID: {} and bookID: {}", userID, bookID);

        Optional<UserEntity> userWithBooks = userRepository.fetchWithBooks(userID);

        if (userWithBooks.isEmpty()) {
            log.error("User with userID: {} not found", userID);

            throw new EntityNotFoundException("User", Map.of("userID", userID.toString()));
        }

        UsersBookEntity existingBook = userWithBooks.get()
                                                    .getBooks()
                                                    .stream()
                                                    .filter(book -> book.getBook().getBookID().equals(bookID))
                                                    .findFirst()
                                                    .orElse(null);

        if (existingBook != null) {
            log.error("Book with bookID: {} already exists in user's library", bookID);

            throw new EntityExistsException(BOOK, Map.of(BOOK_ID, bookID.toString()));
        }

        log.info("Book with bookID: {} not found in user's library. Adding book to library", bookID);

        BookEntity book = bookRepository.findById(bookID)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                BOOK,
                                                Map.of(BOOK_ID, bookID.toString())
                                        ));

        if (usersBookUpsert.review() != null) {
            userWithBooks.get().addBookToUserLibrary(
                    book,
                    usersBookUpsert.rating(),
                    usersBookUpsert.review()
            );

            log.info("Book with bookID: {} added to user's library with rating and review", bookID);

            return userMapper.entityToUsersBook(book, usersBookUpsert.rating(), usersBookUpsert.review());
        } else {
            userWithBooks.get().addBookToUserLibrary(book, usersBookUpsert.rating());
            log.info("Book with bookID: {} added to user's library with rating", bookID);

            return userMapper.entityToUsersBook(book, usersBookUpsert.rating());
        }
    }
}