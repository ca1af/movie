package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MovieServiceImplTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void postMovieUniqueViolenceTest() {
        // given
        Movie movie = Movie.builder()
                .releaseDate(1L)
                .posterImageUrl("poster_")
                .movieName("Movie " + "1")
                .director("Director ")
                .genre("Genre ")
                .build();

        // then
        assertThrows(DataIntegrityViolationException.class, () -> movieRepository.save(movie));
    }
}