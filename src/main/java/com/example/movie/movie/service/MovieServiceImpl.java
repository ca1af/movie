package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieRequestDto;
import com.example.movie.movie.dto.MovieRequestRecord;
import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Log4j2
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<MovieResponseDto> getMovies() {
        return movieRepository.getMoviesDefault();
    }

    @Override
    @Transactional(readOnly = true)
    public MovieResponseDto getMovieById(Long movieId) {
        Movie movie = movieRepository.findByIdAndInUseIsTrue(movieId).orElseThrow(
                () -> new NoSuchElementException("해당하는 영화를 찾을 수 없습니다")
        );

        return MovieResponseDto.of(movie);
    }

    @Override
    @Transactional
    public MovieResponseDto createMovie(MovieRequestRecord movieRequestDto) {
        Movie movie = Movie.builder()
                .movieName(movieRequestDto.movieName())
                .genre(movieRequestDto.genre())
                .director(movieRequestDto.director())
                .posterImageUrl(movieRequestDto.postImageUrl())
                .releaseDate(movieRequestDto.releaseDate())
                .synopsis(movieRequestDto.synopsis())
                .runningTime(movieRequestDto.runningTime())
                .originalTitle(movieRequestDto.originalTitle())
                .build();

        movieRepository.save(movie);


        movie.setDirector("foo");

        throw new IllegalArgumentException();

//        return MovieResponseDto.of(movie);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT)
    public void updateMovie(Long movieId, MovieRequestRecord movieRequestDto) {
        Movie movie = movieRepository.findByIdAndInUseIsTrue(movieId).orElseThrow(
                () -> new NoSuchElementException("해당하는 영화가 없습니다")
        );

        movie.updateMovie(movieRequestDto);
    }


    @Override
    @Transactional
    public void deleteMovie(Long movieId) {
        movieRepository.deleteMovieById(movieId);
    }
}
