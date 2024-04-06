--liquibase formatted sql

--changeset walthersmulders:1

INSERT INTO genre_movie (genre_movie_id, genre, external_id)
VALUES (gen_random_uuid(), 'Action', 28),
       (gen_random_uuid(), 'Adventure', 12),
       (gen_random_uuid(), 'Animation', 16),
       (gen_random_uuid(), 'Comedy', 35),
       (gen_random_uuid(), 'Crime', 80),
       (gen_random_uuid(), 'Documentary', 99),
       (gen_random_uuid(), 'Drama', 18),
       (gen_random_uuid(), 'Family', 10751),
       (gen_random_uuid(), 'Fantasy', 14),
       (gen_random_uuid(), 'History', 36),
       (gen_random_uuid(), 'Horror', 27),
       (gen_random_uuid(), 'Music', 10402),
       (gen_random_uuid(), 'Mystery', 9648),
       (gen_random_uuid(), 'Romance', 10749),
       (gen_random_uuid(), 'Science Fiction', 878),
       (gen_random_uuid(), 'TV Movie', 10770),
       (gen_random_uuid(), 'Thriller', 53),
       (gen_random_uuid(), 'War', 10752),
       (gen_random_uuid(), 'Western', 37);
