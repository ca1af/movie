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
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMovieListNoContent() throws Exception {
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
}