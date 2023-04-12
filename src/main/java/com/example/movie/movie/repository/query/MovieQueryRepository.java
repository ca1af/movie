package com.example.movie.movie.repository.query;

import com.example.movie.movie.dto.MovieResponseDto;

import java.util.List;

public interface MovieQueryRepository {
    List<MovieResponseDto> searchMovieByCond(MovieSearchCond movieSearchCond);
}
