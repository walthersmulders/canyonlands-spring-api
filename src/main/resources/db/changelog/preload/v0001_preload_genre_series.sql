--liquibase formatted sql

--changeset walthersmulders:1

INSERT INTO genre_series (genre_series_id, genre, external_id)
VALUES (gen_random_uuid(), 'Action & Adventure', 10759),
       (gen_random_uuid(), 'Animation', 16),
       (gen_random_uuid(), 'Comedy', 35),
       (gen_random_uuid(), 'Crime', 80),
       (gen_random_uuid(), 'Documentary', 99),
       (gen_random_uuid(), 'Drama', 18),
       (gen_random_uuid(), 'Family', 10751),
       (gen_random_uuid(), 'Kids', 10762),
       (gen_random_uuid(), 'Mystery', 9648),
       (gen_random_uuid(), 'News', 10763),
       (gen_random_uuid(), 'Reality', 10764),
       (gen_random_uuid(), 'Sci-Fi & Fantasy', 10765),
       (gen_random_uuid(), 'Soap', 10766),
       (gen_random_uuid(), 'Talk', 10767),
       (gen_random_uuid(), 'War & Politics', 10768),
       (gen_random_uuid(), 'Western', 37);
