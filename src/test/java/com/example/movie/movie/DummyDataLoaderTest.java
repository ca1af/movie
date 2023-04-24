package com.example.movie.movie;

import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.JdbcBulkInsertRepository;
import com.example.movie.movie.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@SpringBootTest
class DummyDataLoaderTest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private JdbcBulkInsertRepository jdbcBulkInsertRepository;

    @Test
    void jpaBulkInsert(){
        //given
        List<Movie> movies = IntStream.rangeClosed(21, 10021)
                .mapToObj(i -> Movie.builder()
                        .releaseDate((long) i)
                        .posterImageUrl("poster_" + i)
                        .movieName("Movie " + i)
                        .director("Director " + i)
                        .genre("Genre " + i)
                        .originalTitle("Original Title " + i)
                        .synopsis("Synopsis " + i)
                        .runningTime(120)
                        .build())
                .collect(Collectors.toList());

        //when
        movieRepository.saveAll(movies);
    }

    @Test
    void jdbcBulkInsert(){
        //given
        List<Movie> movies = IntStream.rangeClosed(10022, 20021)
                .mapToObj(i -> Movie.builder()
                        .releaseDate((long) i)
                        .posterImageUrl("poster_" + i)
                        .movieName("Movie " + i)
                        .director("Director " + i)
                        .genre("Genre " + i)
                        .originalTitle("Original Title " + i)
                        .synopsis("Synopsis " + i)
                        .runningTime(120)
                        .build())
                .collect(Collectors.toList());

        //when
        jdbcBulkInsertRepository.bulkInsertMovies(movies);
    }

    // 두 매서드의 10000건 테스트 결과 -> 772 / 123
}