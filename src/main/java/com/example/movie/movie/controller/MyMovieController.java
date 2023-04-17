package com.example.movie.movie.controller;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.repository.query.MovieSearchCond;
import com.example.movie.movie.service.MyMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyMovieController {

    private final MyMovieService movieService;

    @GetMapping("/api/v1/movies/pages/{pageNum}")
    public ResponseEntity<List<MovieResponseDto>> getMoviesPaging(@PathVariable Long pageNum) {
        List<MovieResponseDto> movieList = movieService.getMoviesPaging(pageNum);
        if (movieList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movieList);
        }
    }

    @GetMapping("/api/v1/movies/cond") // cond?
    public ResponseEntity<List<MovieResponseDto>> moviesBySearchCond(@RequestBody MovieSearchCond movieSearchCond){
        List<MovieResponseDto> movieList = movieService.getMoviesBySearchCond(movieSearchCond);
        if (movieList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movieList);
        }
    }

    @PatchMapping("/api/v1/movies/{movieId}")
    public ResponseEntity<Void> softDeleteMovie(@PathVariable Long movieId){
        movieService.softDeleteMovie(movieId);
        return ResponseEntity.ok().build();
    }
}
