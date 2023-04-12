package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieRequestDto;
import com.example.movie.movie.dto.MovieResponseDto;

import java.util.List;

public interface MovieService {
    // 먼저 기능 정리
    List<MovieResponseDto> getMovieList();

    MovieResponseDto getMovieById(Long movieId);

    MovieResponseDto createMovie(MovieRequestDto movieRequestDto);

    void updateMovie(Long movie_id, MovieRequestDto movieRequestDto);

    void deleteMovie(Long movie_id);

    // 추가적으로 조회 로직(searchByCond) 등이 추가 될 수 있을 듯 하다.
}
