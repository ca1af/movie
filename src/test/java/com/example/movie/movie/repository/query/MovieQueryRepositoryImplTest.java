package com.example.movie.movie.repository.query;

import com.example.movie.movie.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;
class MovieQueryRepositoryImplTest {
    @InjectMocks
    private MovieRepository movieRepository;
    @Test
    void searchByCondNullTest() {
        // given
        MovieSearchCond searchCond = MovieSearchCond.builder()
                .build();

        // when, then
        assertThrows(IllegalArgumentException.class, () -> {
            movieRepository.getMoviesBySearchCond(searchCond);
        });
    }
}