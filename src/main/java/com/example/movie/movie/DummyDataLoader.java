package com.example.movie.movie;

import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.entity.MovieImage;
import com.example.movie.movie.entity.MovieVideo;
import com.example.movie.movie.repository.MovieImageRepository;
import com.example.movie.movie.repository.MovieRepository;
import com.example.movie.movie.repository.MovieVideoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DummyDataLoader implements CommandLineRunner {
    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;
    private final MovieVideoRepository movieVideoRepository;

    public DummyDataLoader(MovieRepository movieRepository, MovieImageRepository movieImageRepository, MovieVideoRepository movieVideoRepository) {
        this.movieRepository = movieRepository;
        this.movieImageRepository = movieImageRepository;
        this.movieVideoRepository = movieVideoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create dummy data for movies
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

        // Create dummy data for MovieImages
        List<MovieImage> movieImages = new ArrayList<>();
        for (Movie movie : movies) {
            for (int i = 1; i <= 5; i++) {

                MovieImage movieImage = MovieImage.builder()
                        .movie(movie)
                        .imageUrl("image_" + i + "_for_movie_" + movie.getId())
                        .build();

                movieImages.add(movieImage);
            }
        }
        movieImageRepository.saveAll(movieImages);

        // Create dummy data for MovieVideos
        List<MovieVideo> movieVideos = new ArrayList<>();
        for (Movie movie : movies) {
            for (int i = 1; i <= 3; i++) {
                MovieVideo movieVideo = MovieVideo.builder()
                        .movie(movie)
                        .videoUrl("video_" + i + "_for_movie_" + movie.getId()).build();

                movieVideos.add(movieVideo);
            }
        }
        movieVideoRepository.saveAll(movieVideos);
    }
}

