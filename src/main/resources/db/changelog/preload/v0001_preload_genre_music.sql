--liquibase formatted sql

--changeset walthersmulders:1

INSERT INTO genre_music (genre_music_id, genre)
VALUES (gen_random_uuid(), 'Pop'),
       (gen_random_uuid(), 'Hip-Hop/Rap'),
       (gen_random_uuid(), 'Rock'),
       (gen_random_uuid(), 'Metal'),
       (gen_random_uuid(), 'Electronic'),
       (gen_random_uuid(), 'R&B/Soul'),
       (gen_random_uuid(), 'Country'),
       (gen_random_uuid(), 'Classical'),
       (gen_random_uuid(), 'Jazz'),
       (gen_random_uuid(), 'Folk'),
       (gen_random_uuid(), 'Heavy metal'),
       (gen_random_uuid(), 'Reggae'),
       (gen_random_uuid(), 'Dance'),
       (gen_random_uuid(), 'Blues'),
       (gen_random_uuid(), 'Funk'),
       (gen_random_uuid(), 'Latin');
