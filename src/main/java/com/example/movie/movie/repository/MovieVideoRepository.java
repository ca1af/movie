package com.example.movie.movie.repository;

import com.example.movie.movie.entity.MovieVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieVideoRepository extends JpaRepository<MovieVideo, Long> {
}
