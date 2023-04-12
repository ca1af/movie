package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieRequestDto;
import com.example.movie.movie.dto.MovieResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface MovieService {
    // 먼저 기능 정리
    List<MovieResponseDto> movieList();
    MovieResponseDto getMovieById();
    void createMovie();
    void updateMovie(Long movie_id,  MovieRequestDto movieRequestDto);
    void deleteMovie(Long movie_id);

    // 추가적으로 조회 로직(searchByCond) 등이 추가 될 수 있을 듯 하다.
}
