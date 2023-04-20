package com.example.movie.movie.repository.query;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MovieSearchCond {
    // 1. 영화 이름으로 검색
    // 2. 영화 감독 이름으로 검색 정도?

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$", message = "영화 이름은 1~10글자의 한글, 영어, 숫자로 이루어져야 합니다.")
    private String movieName;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$", message = "감독 이름은 1~10글자의 한글, 영어, 숫자로 이루어져야 합니다.")
    private String director;

    @Builder
    public MovieSearchCond(String movieName, String director) {
        this.movieName = movieName;
        this.director = director;
    }
}
