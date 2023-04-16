package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.repository.query.MovieSearchCond;

import java.util.List;

public interface MyMovieService {
    List<MovieResponseDto> getMoviesPaging(Long pageNum);

    List<MovieResponseDto> getMoviesBySearchCond(MovieSearchCond movieSearchCond);

    void softDeleteMovie(Long movieId);
}
