package com.example.movie.movie.controller;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringJUnitConfig
@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMovieListNoContentTest() throws Exception {
        // Given
        Long pageNum = 10L;

        // When
        List<MovieResponseDto> movieList = new ArrayList<>();

        when(movieService.getMovies(pageNum)).thenReturn(movieList);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies/pages/{pageNum}", pageNum)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        verify(movieService, times(1)).getMovies(pageNum);
    }

    @Test
    void testGetMoviesDefault() throws Exception {
        // Given
        List<MovieResponseDto> movieList = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            // 20개의 더미 MovieResponseDto 객체를 생성하여 movieList에 추가
            MovieResponseDto movie = new MovieResponseDto();
            movieList.add(movie);
        }

        when(movieService.getMoviesDefault()).thenReturn(movieList);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(20));

        // Then
        verify(movieService, times(1)).getMoviesDefault();
    }


    @Test
    void getMoviesDefaultTestNoContent() throws Exception {
        List<MovieResponseDto> movieList = new ArrayList<>();
        when(movieService.getMoviesDefault()).thenReturn(movieList);

        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Then
        verify(movieService, times(1)).getMoviesDefault();
    }
}