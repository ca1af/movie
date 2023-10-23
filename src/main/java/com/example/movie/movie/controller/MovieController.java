package com.example.movie.movie.controller;

import com.example.movie.common.aop.CountExeByMovieId;
import com.example.movie.movie.dto.MovieRequestDto;
import com.example.movie.movie.dto.MovieRequestRecord;
import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<List<MovieResponseDto>> getMoviesDefault() {
        List<MovieResponseDto> movieList = movieService.getMovies();
        if (movieList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movieList);
    }

    @GetMapping("/movies/{movieId}")
    @CountExeByMovieId // 특정 movieId를 통해 get하므로, 여기다 찍어주자
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable Long movieId){
        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }
    @PostMapping("/movies")
    public ResponseEntity<Void> createMovie(@RequestBody @Valid MovieRequestRecord movieRequestDto){
        MovieResponseDto movie = movieService.createMovie(movieRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(movie.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/movies/{movieId}")
    public ResponseEntity<Void> updateMovie(@PathVariable Long movieId, @RequestBody @Valid MovieRequestRecord movieRequestDto){
        movieService.updateMovie(movieId, movieRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/movies/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId){
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }
}
