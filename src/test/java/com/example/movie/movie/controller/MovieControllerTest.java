package com.example.movie.movie.controller;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.repository.MovieRepository;
import com.example.movie.movie.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Test
    void testGetMoviesDefault() throws Exception {
        // given
        List<MovieResponseDto> movieList = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            // 20개의 더미 MovieResponseDto 객체를 생성하여 movieList에 추가
            MovieResponseDto movie = new MovieResponseDto();
            movieList.add(movie);
        }

        when(movieService.getMovies()).thenReturn(movieList);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(20));

        // then
        verify(movieService, times(1)).getMovies();
    }


    @Test
    void getMoviesDefaultTestNoContent() throws Exception {
        List<MovieResponseDto> movieList = new ArrayList<>();
        //given
        when(movieService.getMovies()).thenReturn(movieList);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.json1").doesNotExist());

        // then
        verify(movieService, times(1)).getMovies();
    }
}