package com.example.movie.movie.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MovieRequestDto {
    private Long releaseDate;
    private String movieName;
    @Pattern(regexp = "[가-힣a-zA-Z0-9]*", message = "장르는 한글, 영어, 숫자로만 이루어져야 합니다.")
    private String genre;
    private String director;
    private String postImageUrl;
    private String originalTitle;
    private Integer runningTime;
    private String synopsis;
}
