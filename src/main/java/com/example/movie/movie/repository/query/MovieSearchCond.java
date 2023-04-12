package com.example.movie.movie.repository.query;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MovieSearchCond {
    // 1. 영화 이름으로 검색
    // 2. 영화 감독 이름으로 검색 정도?

    private String movieName;

    private String director;

    @Builder
    public MovieSearchCond(String movieName, String director) {
        this.movieName = movieName;
        this.director = director;
    }
}
