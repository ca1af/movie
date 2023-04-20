package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.MovieRepository;
import com.example.movie.movie.repository.query.MovieSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
@Service
@RequiredArgsConstructor
public class MyMovieServiceImpl implements MyMovieService {

    private final MovieRepository movieRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponseDto> getMoviesPaging(Long pageNum) {
        return movieRepository.getMoviesPaging(pageNum);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponseDto> getMoviesBySearchCond(MovieSearchCond movieSearchCond) {
        return movieRepository.getMoviesBySearchCond(movieSearchCond);
    }

    @Override
    @Transactional
    public void softDeleteMovie(Long movieId) {
        Movie movie = movieRepository.findByIdAndInUseIsTrue(movieId).orElseThrow(
                () -> new NoSuchElementException("해당하는 영화가 없습니다")
        );

        movie.softDeleteMovie();
    }
}
