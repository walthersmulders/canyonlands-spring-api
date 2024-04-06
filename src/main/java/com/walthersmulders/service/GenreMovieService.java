package com.walthersmulders.service;

import com.walthersmulders.mapstruct.dto.genremovie.GenreMovie;
import com.walthersmulders.mapstruct.mapper.GenreMovieMapper;
import com.walthersmulders.persistance.entity.GenreMovieEntity;
import com.walthersmulders.persistance.repository.GenreMovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class GenreMovieService {
    private final GenreMovieRepository genreMovieRepository;
    private final GenreMovieMapper     genreMovieMapper;

    public GenreMovieService(
            GenreMovieRepository genreMovieRepository,
            GenreMovieMapper genreMovieMapper
    ) {
        this.genreMovieRepository = genreMovieRepository;
        this.genreMovieMapper = genreMovieMapper;
    }

    public List<GenreMovie> getGenres() {
        log.info("Getting all genres:movie");

        List<GenreMovieEntity> genreMovies = genreMovieRepository.findAll(Sort.by("genre")
                                                                              .ascending()
        );

        log.info("Found {} genres:movie", genreMovies.size());

        return genreMovies.isEmpty() ? Collections.emptyList()
                                     : genreMovies.stream()
                                                  .map(genreMovieMapper::entityToGenreMovie)
                                                  .toList();
    }
}
