package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieRequestDto;
import com.example.movie.movie.dto.MovieRequestRecord;
import com.example.movie.movie.dto.MovieResponseDto;

import java.util.List;

public interface MovieService {
    // 먼저 기능 정리
    List<MovieResponseDto> getMovies();

    MovieResponseDto getMovieById(Long movieId);

    void updateMovie(Long movieId, MovieRequestRecord movieRequestDto);

    void deleteMovie(Long movieId);

    MovieResponseDto createMovie(MovieRequestRecord movieRequestDto);

}
