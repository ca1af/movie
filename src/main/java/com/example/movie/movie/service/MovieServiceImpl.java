package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieRequestDto;
import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    @Override
    public List<MovieResponseDto> getMovieList() {
        return null;
    }

    @Override
    public MovieResponseDto getMovieById() {
        return null;
    }

    @Override
    public void createMovie() {

    }

    @Override
    public void updateMovie(Long movie_id, MovieRequestDto movieRequestDto) {

    }

    @Override
    public void deleteMovie(Long movie_id) {

    }
}
