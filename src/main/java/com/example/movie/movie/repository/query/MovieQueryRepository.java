package com.example.movie.movie.repository.query;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieQueryRepository {
    List<MovieResponseDto> getMoviesDefault();
    List<MovieResponseDto> getMovies(Long pageNum);
    List<MovieResponseDto> searchMovieByCond(MovieSearchCond movieSearchCond);
    Optional<Movie> findByIdAndInUseIsTrue(Long movieId);
    void deleteMovieById(Long movieId);

    void softDeleteMovieById(Long movieId);
}
