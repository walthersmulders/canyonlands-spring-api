--liquibase formatted sql

--changeset walthersmulders:1

INSERT INTO genre_series (genre_series_id, genre)
VALUES (gen_random_uuid(), 'Action & Adventure'),
       (gen_random_uuid(), 'Animation'),
       (gen_random_uuid(), 'Comedy'),
       (gen_random_uuid(), 'Crime'),
       (gen_random_uuid(), 'Documentary'),
       (gen_random_uuid(), 'Drama'),
       (gen_random_uuid(), 'Family'),
       (gen_random_uuid(), 'Kids'),
       (gen_random_uuid(), 'Mystery'),
       (gen_random_uuid(), 'News'),
       (gen_random_uuid(), 'Reality'),
       (gen_random_uuid(), 'Sci-Fi & Fantasy'),
       (gen_random_uuid(), 'Soap'),
       (gen_random_uuid(), 'Talk'),
       (gen_random_uuid(), 'War & Politics'),
       (gen_random_uuid(), 'Western');
