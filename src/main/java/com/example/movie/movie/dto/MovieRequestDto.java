package com.example.movie.movie.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MovieRequestDto {
    private Long releaseDate;
    private String movieName;
    private String genre;
    private String director;
    private String postImageUrl;
}
