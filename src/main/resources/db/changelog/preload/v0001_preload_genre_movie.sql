--liquibase formatted sql

--changeset walthersmulders:1

INSERT INTO genre_movie (genre_movie_id, genre)
VALUES (gen_random_uuid(), 'Action'),
       (gen_random_uuid(), 'Adventure'),
       (gen_random_uuid(), 'Animation'),
       (gen_random_uuid(), 'Comedy'),
       (gen_random_uuid(), 'Crime'),
       (gen_random_uuid(), 'Documentary'),
       (gen_random_uuid(), 'Drama'),
       (gen_random_uuid(), 'Family'),
       (gen_random_uuid(), 'Fantasy'),
       (gen_random_uuid(), 'History'),
       (gen_random_uuid(), 'Horror'),
       (gen_random_uuid(), 'Music'),
       (gen_random_uuid(), 'Mystery'),
       (gen_random_uuid(), 'Romance'),
       (gen_random_uuid(), 'Science Fiction'),
       (gen_random_uuid(), 'TV Movie'),
       (gen_random_uuid(), 'Thriller'),
       (gen_random_uuid(), 'War'),
       (gen_random_uuid(), 'Western');
