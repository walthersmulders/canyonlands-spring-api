package com.walthersmulders.service.book;

import com.walthersmulders.exception.EntityExistsException;
import com.walthersmulders.exception.EntityNotFoundException;
import com.walthersmulders.exception.GenericBadRequestException;
import com.walthersmulders.mapstruct.dto.author.Author;
import com.walthersmulders.mapstruct.dto.author.AuthorUpsert;
import com.walthersmulders.mapstruct.dto.author.AuthorWithBooks;
import com.walthersmulders.mapstruct.dto.book.Book;
import com.walthersmulders.mapstruct.dto.book.BookUpsert;
import com.walthersmulders.mapstruct.dto.book.BookWithLinks;
import com.walthersmulders.mapstruct.dto.book.BookWithLinksUpsert;
import com.walthersmulders.mapstruct.mapper.AuthorMapper;
import com.walthersmulders.mapstruct.mapper.BookMapper;
import com.walthersmulders.persistence.entity.book.AuthorBookEntity;
import com.walthersmulders.persistence.entity.book.AuthorEntity;
import com.walthersmulders.persistence.entity.book.BookEntity;
import com.walthersmulders.persistence.entity.genre.GenreBookEntity;
import com.walthersmulders.persistence.repository.book.AuthorRepository;
import com.walthersmulders.persistence.repository.book.BookRepository;
import com.walthersmulders.persistence.repository.genre.GenreBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Map.entry;

@Service
@Slf4j
public class AuthorBookService {
    private static final String AUTHOR    = "Author";
    private static final String AUTHOR_ID = "authorID";
    private static final String BOOK      = "Book";
    private static final String BOOK_ID   = "bookID";

    private final AuthorRepository    authorRepository;
    private final AuthorMapper        authorMapper;
    private final BookRepository      bookRepository;
    private final BookMapper          bookMapper;
    private final GenreBookRepository genreBookRepository;

    public AuthorBookService(
            AuthorRepository authorRepository,
            AuthorMapper authorMapper,
            BookRepository bookRepository,
            BookMapper bookMapper,
            GenreBookRepository genreBookRepository
    ) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.genreBookRepository = genreBookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookWithLinks> getBooksWithLinks() {
        log.info("Getting all books with links");

        List<BookEntity> booksWithLinks = bookRepository.fetchBooksWithLinks();

        log.info("Found {} books with links", booksWithLinks.size());

        return booksWithLinks.isEmpty() ? Collections.emptyList()
                                        : booksWithLinks.stream()
                                                        .map(bookMapper::entityToBookWithLinks)
                                                        .toList();
    }

    public List<Book> getBooks() {
        log.info("Getting all books");

        List<BookEntity> books = bookRepository.findAll();

        log.info("Found {} books", books.size());

        return books.isEmpty() ? Collections.emptyList()
                               : books.stream()
                                      .map(bookMapper::entityToBook)
                                      .toList();
    }

    public Author createAuthor(AuthorUpsert authorUpsert) {
        log.info("Creating author");

        boolean exists = authorRepository.exists(authorUpsert.firstName(), authorUpsert.lastName());

        if (exists) {
            log.error("Author already exists");

            throw new EntityExistsException(
                    AUTHOR, Map.ofEntries(
                    entry("first name", authorUpsert.firstName()),
                    entry("last name", authorUpsert.lastName())
            ));
        }

        AuthorEntity authorEntity = authorMapper.authorUpsertToEntity(authorUpsert);

        authorRepository.save(authorEntity);

        log.info("Author created with id {}", authorEntity.getAuthorID());

        return authorMapper.entityToAuthor(authorEntity);
    }

    public List<Author> getAuthors() {
        log.info("Getting all authors");

        List<AuthorEntity> authors = authorRepository.findAll();

        log.info("Found {} authors", authors.size());

        return authors.isEmpty() ? Collections.emptyList()
                                 : authors.stream()
                                          .map(authorMapper::entityToAuthor)
                                          .toList();
    }

    @Transactional
    public BookWithLinks createBookWithAuthors(BookWithLinksUpsert bookWithLinksUpsert) {
        log.info("Creating book with authors");
        log.info("Check if book with ISBN {} already exists", bookWithLinksUpsert.book().isbn());

        boolean existsByIsbn = bookRepository.existsByIsbn(bookWithLinksUpsert.book().isbn());

        log.info("Check if book with title {} already exists", bookWithLinksUpsert.book().title());

        boolean existsByTitle = bookRepository.existsByTitle(bookWithLinksUpsert.book().title());

        Map<String, String> errorsMap = new HashMap<>();

        if (existsByTitle || existsByIsbn) {
            if (existsByIsbn) {
                errorsMap.put("isbn", bookWithLinksUpsert.book().isbn());
            }

            if (existsByTitle) {
                errorsMap.put("title", bookWithLinksUpsert.book().title());
            }

            throw new EntityExistsException(BOOK, errorsMap);
        }

        Optional<GenreBookEntity> bookGenre = genreBookRepository.findById(bookWithLinksUpsert.bookGenreID());

        if (bookGenre.isEmpty()) {
            log.error("Book genre with id {} not found", bookWithLinksUpsert.bookGenreID());

            throw new EntityNotFoundException(
                    "Book Genre",
                    Map.of("bookGenreId", String.valueOf(bookWithLinksUpsert.bookGenreID()))
            );
        }

        BookEntity book = bookMapper.bookUpsertToEntity(bookWithLinksUpsert.book());

        book.setGenreBook(bookGenre.get());
        book.setDateAdded(LocalDateTime.now());
        book.setDateUpdated(LocalDateTime.now());

        bookWithLinksUpsert.authorIDs()
                           .stream()
                           .map(authorRepository::findById)
                           .forEach(author -> author.ifPresent(book::addAuthor));

        // TODO :: check if authors not found for Ids and assoc with book empty then thr error

        bookRepository.save(book);

        return bookMapper.entityToBookWithLinks(book);
    }

    public Author getAuthor(UUID id) {
        log.info("Getting author with authorID {}", id);

        AuthorEntity author = authorRepository.
                findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        AUTHOR,
                        Map.of(AUTHOR_ID, id.toString())
                ));

        return authorMapper.entityToAuthor(author);
    }

    @Transactional(readOnly = true)
    public AuthorWithBooks getAuthorWithBooks(UUID id) {
        log.info("Getting author with books for authorID {}", id);

        AuthorEntity authorWithBooks = authorRepository
                .fetchAuthorWithBooks(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        AUTHOR,
                        Map.of(AUTHOR_ID, id.toString())
                ));

        return authorMapper.entityToAuthorWithBooks(authorWithBooks);
    }

    @Transactional(readOnly = true)
    public List<AuthorWithBooks> getAuthorsWithBooks() {
        log.info("Getting all authors with books");

        List<AuthorEntity> authorsWithBooks = authorRepository.fetchAllWithBooks();

        log.info("Found {} authors", authorsWithBooks.size());

        return authorsWithBooks.isEmpty() ? Collections.emptyList()
                                          : authorsWithBooks.stream()
                                                            .map(authorMapper::entityToAuthorWithBooks)
                                                            .toList();
    }

    public void updateAuthor(UUID id, AuthorUpsert authorUpsert) {
        log.info("Updating author with authorID {}", id);

        Optional<AuthorEntity> existingAuthor = authorRepository.findById(id);

        if (existingAuthor.isEmpty()) {
            log.error("Author with authorID {} not found", id);

            throw new EntityNotFoundException(
                    AUTHOR,
                    Map.of(AUTHOR_ID, id.toString())
            );
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingAuthor.get().checkUpdateDtoEqualsEntity(authorUpsert)
            && Objects.equals(existingAuthor.get().getAdditionalName(), authorUpsert.additionalName())
        ) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            AuthorEntity updatedAuthor = authorMapper.authorEntityUpdateMerge(
                    existingAuthor.get(),
                    authorUpsert
            );

            authorRepository.save(updatedAuthor);

            log.info("Author updated with authorID {}", id);
        }
    }

    public Book getBook(UUID id) {
        log.info("Getting book with bookID {}", id);

        BookEntity book = bookRepository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                BOOK,
                                                Map.of(BOOK_ID, id.toString())
                                        ));

        return bookMapper.entityToBook(book);
    }

    @Transactional(readOnly = true)
    public BookWithLinks getBookWithLinks(UUID id) {
        log.info("Getting book with links for bookID {}", id);

        BookEntity bookWithLinks = bookRepository.fetchBookWithLinks(id)
                                                 .orElseThrow(() -> new EntityNotFoundException(
                                                         BOOK,
                                                         Map.of(BOOK_ID, id.toString())
                                                 ));

        return bookMapper.entityToBookWithLinks(bookWithLinks);
    }

    public void updateBook(UUID id, BookUpsert bookUpsert) {
        log.info("Updating book with bookID {}", id);

        Optional<BookEntity> existingBook = bookRepository.findById(id);

        if (existingBook.isEmpty()) {
            log.error("Book with bookID {} not found", id);

            throw new EntityNotFoundException(
                    BOOK,
                    Map.of(BOOK_ID, id.toString())
            );
        }

        log.info("Check if incoming object has the same fields as existing");

        if (existingBook.get().checkUpdateDtoEqualsEntity(bookUpsert)) {
            log.info("Incoming object has the same fields as existing, no need to update");
        } else {
            log.info("Incoming object has different fields as existing, updating");

            boolean existsByIsbn = false;
            boolean existsByTitle = false;

            log.info("Check if incoming ISBN {} has the same value as existing", bookUpsert.isbn());
            if (!existingBook.get().getIsbn().equals(bookUpsert.isbn())) {
                log.info("Check if book with ISBN {} already exists", bookUpsert.isbn());

                existsByIsbn = bookRepository.existsByIsbn(bookUpsert.isbn());
            }

            log.info("Check if book with title {} already exists", bookUpsert.title());
            if (!existingBook.get().getTitle().equals(bookUpsert.title())) {
                existsByTitle = bookRepository.existsByTitle(bookUpsert.title());
            }

            Map<String, String> errorsMap = new HashMap<>();

            if (existsByTitle || existsByIsbn) {
                if (existsByIsbn) {
                    errorsMap.put("isbn", bookUpsert.isbn());
                }

                if (existsByTitle) {
                    errorsMap.put("title", bookUpsert.title());
                }

                throw new EntityExistsException(BOOK, errorsMap);
            }

            BookEntity updatedBook = bookMapper.bookEntityUpdateMerge(
                    existingBook.get(),
                    bookUpsert
            );

            updatedBook.setDateUpdated(LocalDateTime.now());

            bookRepository.save(updatedBook);

            log.info("Book updated with bookID {}", id);
        }
    }

    @Transactional
    public void addAuthorToBook(UUID bookID, UUID authorID) {
        log.info("Adding author with authorID {} to book with bookID {}", authorID, bookID);

        Optional<BookEntity> book = bookRepository.fetchBookWithAuthors(bookID);

        if (book.isEmpty()) {
            log.error("Book with bookID {} not found", bookID);

            throw new EntityNotFoundException(
                    BOOK,
                    Map.of(BOOK_ID, bookID.toString())
            );
        }

        AuthorBookEntity authorBook = book.get().getAuthors()
                                          .stream()
                                          .filter(author -> author.getAuthor().getAuthorID().equals(authorID))
                                          .findFirst()
                                          .orElse(null);

        if (authorBook != null) {
            log.error("Author with authorID {} already exists in books list", authorID);

            throw new EntityExistsException(
                    AUTHOR,
                    Map.of(AUTHOR_ID, authorID.toString())
            );
        }

        Optional<AuthorEntity> author = authorRepository.findById(authorID);

        if (author.isEmpty()) {
            log.error("Author with authorID {} not found", authorID);

            throw new EntityNotFoundException(
                    AUTHOR,
                    Map.of(AUTHOR_ID, authorID.toString())
            );
        }

        book.get().addAuthor(author.get());

        log.info("Author with authorID {} added to book with bookID {}", authorID, bookID);
    }

    @Transactional
    public void removeAuthorFromBook(UUID bookID, UUID authorID) {
        log.info("Removing author with authorID {} from book with bookID {}", authorID, bookID);

        Optional<BookEntity> book = bookRepository.fetchBookWithAuthors(bookID);

        if (book.isEmpty()) {
            log.error("Book with bookID {} not found", bookID);

            throw new EntityNotFoundException(
                    BOOK,
                    Map.of(BOOK_ID, bookID.toString())
            );
        }

        AuthorBookEntity authorBook = book.get().getAuthors()
                                          .stream()
                                          .filter(author -> author.getAuthor().getAuthorID().equals(authorID))
                                          .findFirst()
                                          .orElse(null);

        if (authorBook != null) {
            if (authorBook.getBook().getAuthors().size() <= 1) {
                log.error("A book must have at least one author");

                throw new GenericBadRequestException("A book must have at least one author");
            }

            authorBook.getBook().removeAuthor(authorBook.getAuthor());

            log.info("Author with authorID {} removed from book with bookID {}", authorID, bookID);
        }
    }

    public void updateBookGenre(UUID bookID, UUID genreID) {
        log.info("Updating book genre with genreID {} for book with bookID {}", genreID, bookID);

        Optional<BookEntity> book = bookRepository.findById(bookID);

        if (book.isEmpty()) {
            log.error("Book with bookID {} not found", bookID);

            throw new EntityNotFoundException(
                    BOOK,
                    Map.of(BOOK_ID, bookID.toString())
            );
        }

        Optional<GenreBookEntity> bookGenre = genreBookRepository.findById(genreID);

        if (bookGenre.isEmpty()) {
            log.error("Book genre with genreID {} not found", genreID);

            throw new EntityNotFoundException(
                    "Book Genre",
                    Map.of("bookGenreId", genreID.toString())
            );
        }

        book.get().setGenreBook(bookGenre.get());

        bookRepository.save(book.get());

        log.info("Book genre updated with genreID {} for book with bookID {}", genreID, bookID);
    }
}
