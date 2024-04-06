--liquibase formatted sql

--changeset walthersmulders:1

CREATE TABLE genre_book
(
    book_genre_id UUID         NOT NULL,
    genre         VARCHAR(255) NOT NULL,
    sub_genre     VARCHAR(500) NOT NULL,

    PRIMARY KEY (book_genre_id)
);

CREATE TABLE genre_movie
(
    genre_movie_id UUID         NOT NULL,
    genre          VARCHAR(255) NOT NULL,
    external_id    INTEGER      NOT NULL,

    PRIMARY KEY (genre_movie_id),
    UNIQUE (genre),
    UNIQUE (external_id)
);

CREATE TABLE author
(
    author_id       UUID         NOT NULL,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    additional_name VARCHAR(100),

    PRIMARY KEY (author_id)
);


CREATE TABLE book
(
    book_id        UUID          NOT NULL,
    book_genre_id  UUID          NOT NULL,
    isbn           VARCHAR(20)   NOT NULL,
    title          VARCHAR(255)  NOT NULL,
    pages          INTEGER       NOT NULL,
    plot           TEXT          NOT NULL,
    cover          VARCHAR(1000) NOT NULL,
    date_published DATE          NOT NULL,
    date_added     TIMESTAMP     NOT NULL,
    date_updated   TIMESTAMP     NOT NULL,

    PRIMARY KEY (book_id),
    UNIQUE (isbn),
    UNIQUE (title),
    FOREIGN KEY (book_genre_id) REFERENCES genre_book (book_genre_id)
);

CREATE TABLE author_book
(
    author_id UUID NOT NULL,
    book_id   UUID NOT NULL,

    PRIMARY KEY (author_id, book_id),
    FOREIGN KEY (author_id) REFERENCES author,
    FOREIGN KEY (book_id) REFERENCES book
);

CREATE TABLE users
(
    user_id       UUID         NOT NULL,
    username      VARCHAR(100) NOT NULL,
    email_address VARCHAR(500) NOT NULL,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,

    PRIMARY KEY (user_id),
    UNIQUE (username),
    UNIQUE (email_address)
);

CREATE TABLE users_book
(
    user_id UUID    NOT NULL,
    book_id UUID    NOT NULL,
    rating  INTEGER NOT NULL,
    review  VARCHAR(5000),


    PRIMARY KEY (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES users,
    FOREIGN KEY (book_id) REFERENCES book
);

CREATE TABLE movie
(
    movie_id      UUID          NOT NULL,
    title         VARCHAR(500)  NOT NULL,
    plot          TEXT          NOT NULL,
    external_id   INTEGER       NOT NULL,
    poster        VARCHAR(1000) NOT NULL,
    date_released DATE          NOT NULL,
    date_added    TIMESTAMP     NOT NULL,
    date_updated  TIMESTAMP     NOT NULL,

    PRIMARY KEY (movie_id),
    UNIQUE (title),
    UNIQUE (external_id)
);

CREATE TABLE movie_genre
(
    genre_movie_id UUID NOT NULL,
    movie_id       UUID NOT NULL,

    PRIMARY KEY (genre_movie_id, movie_id),
    FOREIGN KEY (genre_movie_id) REFERENCES genre_movie,
    FOREIGN KEY (movie_id) REFERENCES movie
);


