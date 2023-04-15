package com.example.movie.movie.dto;

import com.example.movie.movie.entity.MovieVideo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MovieVideoResponseDto {

    private Long id;
    private String videoUrl;
    private Long movieId;

    private MovieVideoResponseDto(MovieVideo movieVideo) {
        this.id = movieVideo.getId();
        this.videoUrl = movieVideo.getVideoUrl();
        this.movieId = movieVideo.getMovie().getId();
    }

    public static MovieVideoResponseDto of(MovieVideo movieVideo){
        return new MovieVideoResponseDto(movieVideo);
    }
}
