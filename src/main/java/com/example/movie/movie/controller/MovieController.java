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
    public ResponseEntity<List<MovieResponseDto>> getMovieList(@PathVariable Long pageNum){
        List<MovieResponseDto> movieList = movieService.getMovieList(pageNum);
        if (movieList.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movieList);
        }
    }

    @GetMapping("/api/v1/movies/{movie_id}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable Long movie_id){
        return ResponseEntity.ok(movieService.getMovieById(movie_id));
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

    @PutMapping("/api/v1/movies/{movie_id}")
    public ResponseEntity<Void> updateMovie(@PathVariable Long movie_id, @RequestBody MovieRequestDto movieRequestDto){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/movies/{movie_id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movie_id){
        return ResponseEntity.ok().build();
    }
}
