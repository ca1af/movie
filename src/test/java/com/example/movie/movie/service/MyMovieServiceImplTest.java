package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MyMovieServiceImplTest {
    @Autowired
    private MovieRepository movieRepository;

    @Test
    void getMoviesFailureTest(){
        //when
        List<MovieResponseDto> movieList = movieRepository.getMoviesPaging(6L);
        //then
        assertEquals(0, movieList.size());

        // Controller 테스트에서 상태코드 확인해볼까?
    }

    @Test
    void getMovieListPagingTest() {
        //when
        List<MovieResponseDto> movieList = movieRepository.getMoviesPaging(1L);
        List<MovieResponseDto> movieList2 = movieRepository.getMoviesPaging(2L);
        //then : 페이징 잘 되고 있는지 확인
        assertEquals(movieList.size(), 10);
        assertEquals(movieList2.size(), 10);
    }

    @Test
    void softDeleteDefaultTest(){
        Movie movie = movieRepository.findById(1L).orElseThrow(
                () -> new NoSuchElementException("해당하는 영화가 없습니다")
        );
        movie.softDeleteMovie();

        movieRepository.save(movie);
        // 트랜잭션이 커밋되지 않으므로 더티체킹이 일어나지 않는다.
        // 따라서 명시적으로 save
    }

    @Test
    void softDeleteMovie() {
        //given
        Movie movie = movieRepository.findById(1L).orElseThrow(
                () -> new NoSuchElementException("해당하는 영화가 없습니다")
        );
        //when
        movie.softDeleteMovie();
        movieRepository.save(movie);

        //then
        List<MovieResponseDto> movies = movieRepository.getMoviesPaging(1L);

        assertEquals(2, movies.get(0).getId());
    }

}
