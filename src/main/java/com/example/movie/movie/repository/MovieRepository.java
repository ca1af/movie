package com.example.movie.movie.repository;

import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.query.MovieQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieQueryRepository {

}
