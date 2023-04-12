package com.example.movie.movie.controller;

import com.example.movie.movie.dto.MovieRequestDto;
import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/api/v1/movies")
    public List<MovieResponseDto> getMovieList(){
        return null;
    }

    @GetMapping("/api/v1/movies/{movie_id}")
    public MovieResponseDto getMovieById(@PathVariable Long movie_id){
        return null;
    }

    @PostMapping("/api/v1/movies")
    public void createMovie(){

    }
    @PutMapping("/api/v1/movies/{movie_id}")
    public void updateMovie(@PathVariable Long movie_id, @RequestBody MovieRequestDto movieRequestDto){

    }

    @DeleteMapping("/api/v1/movies/{movie_id}")
    public void deleteMovie(@PathVariable Long movie_id){

    }
}
