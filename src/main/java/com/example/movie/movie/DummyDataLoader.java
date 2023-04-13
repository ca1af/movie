package com.example.movie.movie;

import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DummyDataLoader implements CommandLineRunner {
    private final MovieRepository movieRepository;

    public DummyDataLoader(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Movie> movies = IntStream.rangeClosed(1, 20)
                .mapToObj(i -> Movie.builder()
                        .releaseDate((long) i)
                        .posterImageUrl("poster_" + i)
                        .movieName("Movie " + i)
                        .director("Director " + i)
                        .genre("Genre " + i)
                        .build())
                .collect(Collectors.toList());

        movieRepository.saveAll(movies);
    }
}
