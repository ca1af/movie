package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.MovieRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MovieServiceImplTest {

    @Autowired
    private MovieRepository movieRepository;


    void createDummiesBeforeTest(){
        Movie movie = Movie.builder()
                .releaseDate(1L)
                .posterImageUrl("1")
                .movieName("1")
                .director("1")
                .genre("1")
                .build();
        movieRepository.save(movie);
    }

    @Test
    @Transactional
    void getMovieList() {
        //given
        createDummiesBeforeTest();
        //when
        List<MovieResponseDto> movieList = movieRepository.getMovieList(1L);
        //then
        assertEquals(movieList.size(), 1);
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