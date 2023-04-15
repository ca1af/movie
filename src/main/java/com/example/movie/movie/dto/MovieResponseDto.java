package com.example.movie.movie.dto;

import com.example.movie.movie.entity.Movie;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MovieResponseDto {

    private Long id;
    private Long releaseDate;
    private String movieName;
    private String genre;
    private String director;
    private String postImageUrl;
    private Set<MovieImageResponseDto> movieImages;
    private Set<MovieVideoResponseDto> movieVideos;

    private MovieResponseDto(Movie movie) {
        this.id = movie.getId();
        this.releaseDate = movie.getReleaseDate();
        this.movieName = movie.getMovieName();
        this.genre = movie.getGenre();
        this.director = movie.getDirector();
        this.postImageUrl = movie.getPosterImageUrl();

        this.movieImages = movie.getMovieImages().stream()
                .map(MovieImageResponseDto::of)
                .sorted(Comparator.comparingLong(MovieImageResponseDto::getId))
                // 출력 순서를 id 순으로 보장하는 로직
                .collect(Collectors.toCollection(LinkedHashSet::new));

        this.movieVideos = movie.getMovieVideos().stream()
                .map(MovieVideoResponseDto::of)
                .sorted(Comparator.comparingLong(MovieVideoResponseDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static MovieResponseDto of(Movie movie) {
        return new MovieResponseDto(movie);
    }
}
