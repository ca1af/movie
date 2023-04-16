package com.example.movie.movie.controller;

import com.example.movie.movie.dto.MovieRequestDto;
import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.service.MovieService;
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

    @GetMapping("/api/v1/movies")
    public ResponseEntity<List<MovieResponseDto>> getMoviesDefault() {
        List<MovieResponseDto> movieList = movieService.getMovies();
        if (movieList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movieList);
        }
    }

    @GetMapping("/api/v1/movies/{movieId}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable Long movieId){
        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }

    //PostMan 등으로 확인만 할거라면...그냥 JSon 형태의 데이터를 보여주는 게 낫나?...

    @PostMapping("/api/v1/movies")
    public ResponseEntity<Void> createMovie(@RequestBody MovieRequestDto movieRequestDto){
        MovieResponseDto movie = movieService.createMovie(movieRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(movie.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/api/v1/movies/{movieId}")
    public ResponseEntity<Void> updateMovie(@PathVariable Long movieId, @RequestBody MovieRequestDto movieRequestDto){
        movieService.updateMovie(movieId, movieRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/movies/{movie_id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movie_id){
        movieService.deleteMovie(movie_id);
        return ResponseEntity.noContent().build();
    }
}
