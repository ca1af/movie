package com.example.movie.movie.controller;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.repository.query.MovieSearchCond;
import com.example.movie.movie.service.MyMovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyMovieController {

    private final MyMovieService movieService;
    @GetMapping("/movies/pages/{pageNum}")
    public ResponseEntity<List<MovieResponseDto>> getMoviesPaging(@PathVariable Long pageNum) {
        List<MovieResponseDto> movieList = movieService.getMoviesPaging(pageNum);
        if (movieList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movieList);
    }

    @GetMapping("/movies/search") // cond?
    public ResponseEntity<List<MovieResponseDto>> moviesBySearchCond(@ModelAttribute @Valid MovieSearchCond movieSearchCond) {
        List<MovieResponseDto> movieList = movieService.getMoviesBySearchCond(movieSearchCond);
        if (movieList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movieList);
    }

    @PutMapping("/movies/{movieId}")
    public ResponseEntity<Void> softDeleteMovie(@PathVariable Long movieId){
        movieService.softDeleteMovie(movieId);
        return ResponseEntity.ok().build();
    }
}