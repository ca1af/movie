package com.example.movie.movie.service;

import com.example.movie.movie.dto.MovieRequestDto;
import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
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
    public MovieResponseDto createMovie(MovieRequestDto movieRequestDto) {
        String movieName = movieRequestDto.getMovieName();

        if (movieRepository.existsByMovieName(movieName)) {
            throw new DataIntegrityViolationException("중복된 영화이름이 이미 존재합니다.");
        }

        Movie movie = Movie.builder()
                .movieName(movieRequestDto.getMovieName())
                .genre(movieRequestDto.getGenre())
                .director(movieRequestDto.getDirector())
                .posterImageUrl(movieRequestDto.getPostImageUrl())
                .releaseDate(movieRequestDto.getReleaseDate())
                .synopsis(movieRequestDto.getSynopsis())
                .runningTime(movieRequestDto.getRunningTime())
                .originalTitle(movieRequestDto.getOriginalTitle())
                .build();

        movieRepository.save(movie);

        return MovieResponseDto.of(movie);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT)
    public void updateMovie(Long movieId, MovieRequestDto movieRequestDto) {
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
