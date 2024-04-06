package com.walthersmulders.service;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.users.book.UsersBook;
import com.walthersmulders.mapstruct.dto.users.book.UsersBookUpsert;
import com.walthersmulders.mapstruct.mapper.UserMapper;
import com.walthersmulders.mapstruct.mapper.UsersBookMapper;
import com.walthersmulders.persistance.entity.BookEntity;
import com.walthersmulders.persistance.entity.UserEntity;
import com.walthersmulders.persistance.entity.UsersBookEntity;
import com.walthersmulders.persistance.entity.UsersBookID;
import com.walthersmulders.persistance.repository.BookRepository;
import com.walthersmulders.persistance.repository.UserRepository;
import com.walthersmulders.persistance.repository.UsersBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UsersBookService {
    private static final String BOOK_ID = "bookID";
    private static final String BOOK    = "Book";
    private static final String USER_ID = "userID";

    private final UserRepository      userRepository;
    private final UserMapper          userMapper;
    private final BookRepository      bookRepository;
    private final UsersBookRepository usersBookRepository;
    private final UsersBookMapper     usersBookMapper;

    public UsersBookService(
            UserRepository userRepository,
            UserMapper userMapper,
            BookRepository bookRepository,
            UsersBookRepository usersBookRepository,
            UsersBookMapper usersBookMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.bookRepository = bookRepository;
        this.usersBookRepository = usersBookRepository;
        this.usersBookMapper = usersBookMapper;
    }

    @Transactional
    public UsersBook addBookToUserLibrary(UUID userID, UUID bookID, UsersBookUpsert usersBookUpsert) {
        log.info("Adding book to user library for userID: {} and bookID: {}", userID, bookID);

        Optional<UserEntity> userWithBooks = userRepository.fetchWithBooks(userID);

        if (userWithBooks.isEmpty()) {
            log.error("User with userID: {} not found", userID);

            throw new EntityNotFoundException("User", Map.of(USER_ID, userID.toString()));
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

    @Transactional(readOnly = true)
    public UsersBook getUsersBook(UUID userID, UUID bookID) {
        log.info("Fetching book with bookID: {} for user with userID: {}", bookID, userID);

        Optional<UsersBookEntity> usersBook = usersBookRepository.fetchUsersBook(userID, bookID);

        if (usersBook.isEmpty()) {
            log.error("Combination with userID: {} and bookID: {} not found", userID, bookID);

            throw new EntityNotFoundException("UsersBook", Map.ofEntries(
                    Map.entry(USER_ID, userID.toString()),
                    Map.entry(BOOK_ID, bookID.toString())
            ));
        }

        log.info("Combination with userID: {} and bookID: {} found", userID, bookID);

        return usersBookMapper.entityToUsersBook(usersBook.get());
    }

    public List<UsersBook> getAllUserBooks(UUID userID) {
        log.info("Fetching all books for user with userID: {}", userID);

        List<UsersBookEntity> usersBooks = usersBookRepository.fetchAllUsersBooks(userID);

        log.info("Fetched all books for user with userID: {}", userID);

        return usersBooks.stream().map(usersBookMapper::entityToUsersBook).toList();
    }

    public void removeBookFromUserLibrary(UUID userID, UUID bookID) {
        log.info("Deleting book with bookID: {} for user with userID: {}", bookID, userID);

        usersBookRepository.deleteById(new UsersBookID(userID, bookID));

        log.info("Deleted book with bookID: {} for user with userID: {}", bookID, userID);
    }

    public void update(UUID userID, UUID bookID, UsersBookUpsert usersBookUpsert) {
        log.info("Updating book with bookID: {} for user with userID: {}", bookID, userID);

        Optional<UsersBookEntity> existingUsersBook = usersBookRepository.findById(
                new UsersBookID(userID, bookID)
        );

        if (existingUsersBook.isEmpty()) {
            log.error("Combination with userID: {} and bookID: {} not found", userID, bookID);

            throw new EntityNotFoundException("UsersBook", Map.ofEntries(
                    Map.entry(USER_ID, userID.toString()),
                    Map.entry(BOOK_ID, bookID.toString())
            ));
        }

        existingUsersBook.get().setReview(usersBookUpsert.review());
        existingUsersBook.get().setRating(usersBookUpsert.rating());

        usersBookRepository.save(existingUsersBook.get());

        log.info("Updated book with bookID: {} for user with userID: {}", bookID, userID);
    }
}
