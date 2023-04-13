package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieRequestDto;
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
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MovieResponseDto> getMovieList(Long pageNum) {
        return movieRepository.getMovieList(pageNum);
    }

    @Override
    public List<MovieResponseDto> getMoviesBySearchCond(MovieSearchCond movieSearchCond) {
        return movieRepository.searchMovieByCond(movieSearchCond);
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
    public MovieResponseDto createMovie(MovieRequestDto movieRequestDto) {

        Movie movie = Movie.builder()
                .movieName(movieRequestDto.getMovieName())
                .genre(movieRequestDto.getGenre())
                .director(movieRequestDto.getDirector())
                .posterImageUrl(movieRequestDto.getPostImageUrl())
                .releaseDate(movieRequestDto.getReleaseDate())
                .build();

        movieRepository.save(movie);

        return MovieResponseDto.of(movie);
    }

    @Override
    @Transactional
    public void updateMovie(Long movie_id, MovieRequestDto movieRequestDto) {
        Movie movie = movieRepository.findByIdAndInUseIsTrue(movie_id).orElseThrow(
                () -> new NoSuchElementException("해당하는 영화가 없습니다")
        );
        movie.updateMovie(movieRequestDto);
    }

    @Override
    @Transactional
    public void softDeleteMovie(Long movie_id) {
        Movie movie = movieRepository.findByIdAndInUseIsTrue(movie_id).orElseThrow(
                () -> new NoSuchElementException("해당하는 영화가 없습니다")
        );

        movie.softDeleteMovie(movie);
    }

    @Override
    public void deleteMovie(Long movie_id) {
        Movie movie = movieRepository.findById(movie_id).orElseThrow(
                () -> new NoSuchElementException("해당하는 영화가 없습니다")
        );

        movieRepository.deleteMovieById(movie.getId());
    }
}
