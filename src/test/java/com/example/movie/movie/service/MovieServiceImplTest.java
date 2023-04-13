package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MovieServiceImplTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void getMovieListPagingTest() {
        //when
        List<MovieResponseDto> movieList = movieRepository.getMovies(1L);
        List<MovieResponseDto> movieList2 = movieRepository.getMovies(2L);
        //then : 페이징 잘 되고 있는지 확인
        assertEquals(movieList.size(), 10);
        assertEquals(movieList2.size(), 10);
    }

    @Test
    void getMoviesFailureTest(){
        //when
        List<MovieResponseDto> movieList = movieRepository.getMovies(3L);
        //then
        assertEquals(0, movieList.size());

        // Controller 테스트에서 상태코드 확인해볼까?
    }

    @Test
    void getMovieById() {
    }

    @Test
    void createMovie() {
    }

    @Test
    void updateMovie() {
    }

    @Test
    void softDeleteMovie() {
    }

    @Test
    void deleteMovie() {
    }
}