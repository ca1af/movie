package com.example.movie.movie.dto;

import com.example.movie.movie.entity.MovieImage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieImageResponseDto {

    private Long id;
    private String imageUrl;
    private Long movieId;

    private MovieImageResponseDto(MovieImage movieImage) {
        this.id = movieImage.getId();
        this.imageUrl = movieImage.getImageUrl();
        this.movieId = movieImage.getMovie().getId();
    }

    public static MovieImageResponseDto of(MovieImage movieImage){
        return new MovieImageResponseDto(movieImage);
    }
}
