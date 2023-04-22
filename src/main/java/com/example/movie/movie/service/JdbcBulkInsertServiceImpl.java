package com.example.movie.movie.service;

import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.entity.MovieImage;
import com.example.movie.movie.repository.JdbcBulkInsertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JdbcBulkInsertServiceImpl implements JdbcBulkInsertService {
    private final JdbcBulkInsertRepository jdbcBulkInsertRepository;
    @Override
    public void MoviesBulkInsert(List<Movie> movies){
        jdbcBulkInsertRepository.bulkInsertMovies(movies);
    }
    @Override
    public void MovieImagesBulkInsert(List<MovieImage> movieImages){
        jdbcBulkInsertRepository.bulkInsertMovieImage(movieImages);
    }
}
