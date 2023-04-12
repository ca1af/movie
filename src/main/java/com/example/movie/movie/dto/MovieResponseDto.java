package com.example.movie.movie.dto;

import com.example.movie.movie.entity.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MovieResponseDto {

    private Long id;
    private Long releaseDate;
    private String movieName;
    private String genre;
    private String director;
    private String postImageUrl;

    private MovieResponseDto(Movie movie) {
        this.id = movie.getId();
        this.releaseDate = movie.getReleaseDate();
        this.movieName = movie.getMovieName();
        this.genre = movie.getGenre();
        this.director = movie.getDirector();
        this.postImageUrl = movie.getPosterImageUrl();
    }

    public static MovieResponseDto of(Movie movie) {
        return new MovieResponseDto(movie);
    }
}
