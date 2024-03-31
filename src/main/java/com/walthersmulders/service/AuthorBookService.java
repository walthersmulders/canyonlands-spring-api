package com.walthersmulders.service;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.mapstruct.dto.author.Author;
import com.walthersmulders.mapstruct.dto.author.AuthorNoID;
import com.walthersmulders.mapstruct.dto.author.AuthorWithBooks;
import com.walthersmulders.mapstruct.dto.book.BookWithAuthorsAdd;
import com.walthersmulders.mapstruct.dto.book.BookWithLinks;
import com.walthersmulders.mapstruct.mapper.AuthorMapper;
import com.walthersmulders.mapstruct.mapper.BookMapper;
import com.walthersmulders.persistance.entity.AuthorEntity;
import com.walthersmulders.persistance.entity.BookEntity;
import com.walthersmulders.persistance.entity.BookGenreEntity;
import com.walthersmulders.persistance.repository.AuthorRepository;
import com.walthersmulders.persistance.repository.BookGenreRepository;
import com.walthersmulders.persistance.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Map.entry;

@Service
@Slf4j
public class AuthorBookService {
    private static final String AUTHOR = "Author";

    private final AuthorRepository    authorRepository;
    private final AuthorMapper        authorMapper;
    private final BookRepository      bookRepository;
    private final BookMapper          bookMapper;
    private final BookGenreRepository bookGenreRepository;

    public AuthorBookService(
            AuthorRepository authorRepository, AuthorMapper authorMapper,
            BookRepository bookRepository,
            BookMapper bookMapper,
            BookGenreRepository bookGenreRepository
    ) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.bookGenreRepository = bookGenreRepository;
    }

    @Transactional(readOnly = true)
    public List<BookWithLinks> getBooks() {
        log.info("Getting all books");


        List<BookEntity> booksWithAuthors = bookRepository.fetchAll();

        log.info("Found {} books", booksWithAuthors.size());

        return booksWithAuthors.isEmpty() ? List.of()
                                          : booksWithAuthors.stream()
                                                            .map(bookMapper::entityToBookWithLinks)
                                                            .toList();

    }

    public Author createAuthor(AuthorNoID authorNoID) {
        log.info("Creating author");

        boolean exists = authorRepository.exists(authorNoID.firstName(), authorNoID.lastName());

        if (exists) {
            log.error("Author already exists");

            throw new EntityExistsException(
                    AUTHOR, Map.ofEntries(
                    entry("first name", authorNoID.firstName()),
                    entry("last name", authorNoID.lastName())
            ));
        }

        AuthorEntity authorEntity = authorMapper.authorNoIDToEntity(authorNoID);

        authorRepository.save(authorEntity);

        log.info("Author created with id {}", authorEntity.getAuthorID());

        return authorMapper.entityToAuthor(authorEntity);
    }

    public List<Author> getAuthors() {
        log.info("Getting all authors");

        List<AuthorEntity> authors = authorRepository.findAll();

        log.info("Found {} authors", authors.size());

        return authors.isEmpty() ? List.of()
                                 : authors.stream()
                                          .map(authorMapper::entityToAuthor)
                                          .toList();
    }

    @Transactional
    public BookWithLinks createBookWithAuthors(BookWithAuthorsAdd bookWithAuthorsAdd) {
        log.info("Creating book with authors");
        log.info("Check if book with ISBN {} already exists", bookWithAuthorsAdd.book().isbn());

        boolean existsByIsbn = bookRepository.existsByIsbn(bookWithAuthorsAdd.book().isbn());

        log.info("Check if book with title {} already exists", bookWithAuthorsAdd.book().title());

        boolean existsByTitle = bookRepository.existsByTitle(bookWithAuthorsAdd.book().title());

        Map<String, String> errorsMap = new HashMap<>();

        if (existsByTitle || existsByIsbn) {
            if (existsByIsbn) {
                errorsMap.put("isbn", bookWithAuthorsAdd.book().isbn());
            }

            if (existsByTitle) {
                errorsMap.put("title", bookWithAuthorsAdd.book().title());
            }

            throw new EntityExistsException("Book", errorsMap);
        }

        Optional<BookGenreEntity> bookGenre = bookGenreRepository.findById(bookWithAuthorsAdd.bookGenreID());

        if (bookGenre.isEmpty()) {
            log.error("Book genre with id {} not found", bookWithAuthorsAdd.bookGenreID());

            throw new EntityNotFoundException(
                    "Book Genre",
                    Map.of("bookGenreId", String.valueOf(bookWithAuthorsAdd.bookGenreID()))
            );
        }

        BookEntity book = bookMapper.bookAddToEntity(bookWithAuthorsAdd.book());

        book.setBookGenre(bookGenre.get());
        book.setDateAdded(LocalDateTime.now());
        book.setDateUpdated(LocalDateTime.now());

        bookWithAuthorsAdd.authorIDs()
                          .stream()
                          .map(authorRepository::findById)
                          .forEach(author -> author.ifPresent(book::addAuthor));

        bookRepository.save(book);

        return bookMapper.entityToBookWithLinks(book);
    }

    public Author getAuthor(UUID id) {
        log.info("Getting author with authorID {}", id);

        AuthorEntity author = authorRepository.
                findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        AUTHOR,
                        Map.of("authorID", id.toString())
                ));

        return authorMapper.entityToAuthor(author);
    }

    @Transactional(readOnly = true)
    public AuthorWithBooks getAuthorWithBooks(UUID id) {
        log.info("Getting author with books for authorID {}", id);

        // TODO :: If author does not have a book then the result will be empty, need to handle this

        AuthorEntity authorWithBooks = authorRepository
                .fetchAuthorWithBooks(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        AUTHOR,
                        Map.of("authorID", id.toString())
                ));

        return authorMapper.entityToAuthorWithBooks(authorWithBooks);
    }

    @Transactional(readOnly = true)
    public List<AuthorWithBooks> getAuthorsWithBooks() {
        log.info("Getting all authors with books");

        // TODO :: If author does not have a book then the result will be empty, need to handle this

        List<AuthorEntity> authorsWithBooks = authorRepository.fetchAllWithBooks();

        log.info("Found {} authors", authorsWithBooks.size());

        return authorsWithBooks.isEmpty() ? List.of()
                                          : authorsWithBooks.stream()
                                                            .map(authorMapper::entityToAuthorWithBooks)
                                                            .toList();
    }

    public void updateAuthor(UUID id, AuthorNoID authorNoID) {
        log.info("Updating author with authorID {}", id);

        Optional<AuthorEntity> existingAuthor = authorRepository.findById(id);

        if (existingAuthor.isEmpty()) {
            log.error("Author with authorID {} not found", id);

            throw new EntityNotFoundException(
                    AUTHOR,
                    Map.of("authorID", id.toString())
            );
        }

        AuthorEntity updatedAuthor = authorMapper.authorEntityUpdateMerge(
                existingAuthor.get(),
                authorNoID
        );

        authorRepository.save(updatedAuthor);

        log.info("Author updated with authorID {}", id);
    }
}
