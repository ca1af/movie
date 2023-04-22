package com.example.movie.movie;

import com.example.movie.common.aop.ExeTimer;
import com.example.movie.movie.entity.CastMember;
import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.entity.MovieImage;
import com.example.movie.movie.entity.MovieVideo;
import com.example.movie.movie.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final JdbcBulkInsertRepository jdbcBulkInsertRepository;

    @Override
    @ExeTimer
    public void run(String... args) throws Exception {
        List<Movie> movies = movieRepository.findAll();

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

        jdbcBulkInsertRepository.bulkInsertMovieImage(movieImages);

        List<MovieVideo> movieVideos = new ArrayList<>();
        for (Movie movie : movies) {
            for (int i = 1; i <= 3; i++) {
                MovieVideo movieVideo = MovieVideo.builder()
                        .movie(movie)
                        .videoUrl("video_" + i + "_for_movie_" + movie.getId()).build();

                movieVideos.add(movieVideo);
            }
        }

        jdbcBulkInsertRepository.bulkInsertMovieVideo(movieVideos);

        List<CastMember> castMembers = new ArrayList<>();
        for (Movie movie : movies) {
            for (int i = 1; i <= 3; i++) {
                CastMember castMember = CastMember.builder()
                        .memberName("Cast Member " + i)
                        .movie(movie)
                        .build();

                castMembers.add(castMember);
            }
        }

        jdbcBulkInsertRepository.bulkInsertCastMember(castMembers);
    }

    @PostConstruct
    @ExeTimer
    public void afterRun(){
        List<Movie> movies = IntStream.rangeClosed(1, 20)
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

        jdbcBulkInsertRepository.bulkInsertMovies(movies);
    }
}