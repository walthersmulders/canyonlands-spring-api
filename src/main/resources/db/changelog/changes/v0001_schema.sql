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

    PRIMARY KEY (genre_movie_id),
    UNIQUE (genre)
);

CREATE TABLE genre_series
(
    genre_series_id UUID         NOT NULL,
    genre           VARCHAR(255) NOT NULL,

    PRIMARY KEY (genre_series_id),
    UNIQUE (genre)
);

CREATE TABLE genre_music
(
    genre_music_id UUID         NOT NULL,
    genre          VARCHAR(255) NOT NULL,

    PRIMARY KEY (genre_music_id),
    UNIQUE (genre)
);

CREATE TABLE artist
(
    artist_id       UUID         NOT NULL,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    additional_name VARCHAR(100),

    PRIMARY KEY (artist_id)
);

CREATE TABLE album
(
    album_id       UUID          NOT NULL,
    genre_music_id UUID          NOT NULL,
    title          VARCHAR(255)  NOT NULL,
    cover          VARCHAR(1000) NOT NULL,
    date_published DATE          NOT NULL,
    date_added     TIMESTAMP     NOT NULL,
    date_updated   TIMESTAMP     NOT NULL,

    PRIMARY KEY (album_id),
    UNIQUE (title),
    FOREIGN KEY (genre_music_id) REFERENCES genre_music (genre_music_id)
);

CREATE TABLE artist_album
(
    artist_id UUID NOT NULL,
    album_id  UUID NOT NULL,

    PRIMARY KEY (artist_id, album_id),
    FOREIGN KEY (artist_id) REFERENCES artist,
    FOREIGN KEY (album_id) REFERENCES album
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

CREATE TABLE users_album
(
    user_id  UUID    NOT NULL,
    album_id UUID    NOT NULL,
    rating   INTEGER NOT NULL,
    review   VARCHAR(5000),

    PRIMARY KEY (user_id, album_id),
    FOREIGN KEY (user_id) REFERENCES users,
    FOREIGN KEY (album_id) REFERENCES album
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

CREATE TABLE series
(
    series_id     UUID          NOT NULL,
    title         VARCHAR(500)  NOT NULL,
    plot          TEXT          NOT NULL,
    poster        VARCHAR(1000) NOT NULL,
    date_released DATE          NOT NULL,
    date_added    TIMESTAMP     NOT NULL,
    date_updated  TIMESTAMP     NOT NULL,

    PRIMARY KEY (series_id),
    UNIQUE (title)
);

CREATE TABLE season
(
    season_id     UUID          NOT NULL,
    series_id     UUID          NOT NULL,
    title         VARCHAR(500)  NOT NULL,
    plot          TEXT          NOT NULL,
    poster        VARCHAR(1000) NOT NULL,
    date_released DATE          NOT NULL,
    date_added    TIMESTAMP     NOT NULL,
    date_updated  TIMESTAMP     NOT NULL,

    PRIMARY KEY (season_id),
    FOREIGN KEY (series_id) REFERENCES series,
    UNIQUE (series_id, title)
);

-- UPDATE THIS TO INCLUDE SEASON RATING, WHEN A USER ADDS A SERIES TO THEIR LIBRARY, ADD ALL THE
-- SEASONS IN AS WELL WITH DEFAULT RATING BASED ON THE SERIES RATING, USER CAN LATER ON UPDATE IT
CREATE TABLE users_series
(
    user_id   UUID    NOT NULL,
    series_id UUID    NOT NULL,
    rating    INTEGER NOT NULL,
    review    VARCHAR(5000),

    PRIMARY KEY (user_id, series_id),
    FOREIGN KEY (user_id) REFERENCES users,
    FOREIGN KEY (series_id) REFERENCES series
);

CREATE TABLE series_genre
(
    genre_series_id UUID NOT NULL,
    series_id       UUID NOT NULL,

    PRIMARY KEY (genre_series_id, series_id),
    FOREIGN KEY (genre_series_id) REFERENCES genre_series,
    FOREIGN KEY (series_id) REFERENCES series
);

CREATE TABLE movie
(
    movie_id      UUID          NOT NULL,
    title         VARCHAR(500)  NOT NULL,
    plot          TEXT          NOT NULL,
    poster        VARCHAR(1000) NOT NULL,
    date_released DATE          NOT NULL,
    date_added    TIMESTAMP     NOT NULL,
    date_updated  TIMESTAMP     NOT NULL,

    PRIMARY KEY (movie_id),
    UNIQUE (title)
);

CREATE TABLE movie_genre
(
    genre_movie_id UUID NOT NULL,
    movie_id       UUID NOT NULL,

    PRIMARY KEY (genre_movie_id, movie_id),
    FOREIGN KEY (genre_movie_id) REFERENCES genre_movie,
    FOREIGN KEY (movie_id) REFERENCES movie
);

CREATE TABLE users_movie
(
    user_id  UUID    NOT NULL,
    movie_id UUID    NOT NULL,
    rating   INTEGER NOT NULL,
    review   VARCHAR(5000),

    PRIMARY KEY (user_id, movie_id),
    FOREIGN KEY (user_id) REFERENCES users,
    FOREIGN KEY (movie_id) REFERENCES movie
);
