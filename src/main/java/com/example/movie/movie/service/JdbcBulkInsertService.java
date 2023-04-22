package com.example.movie.movie.service;

import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.entity.MovieImage;

import java.util.List;

public interface JdbcBulkInsertService{
    void MoviesBulkInsert(List<Movie> movies);

    void MovieImagesBulkInsert(List<MovieImage> movieImages);
}
