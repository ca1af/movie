package com.example.movie.movie.controller;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.service.MyMovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
@WebMvcTest(MyMovieController.class)
class MyMovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyMovieService myMovieService;

    @Test
    void getMovieListNoContentTest() throws Exception {
        // given
        Long pageNum = 10L;

        // when
        List<MovieResponseDto> movieList = new ArrayList<>();

        when(myMovieService.getMoviesPaging(pageNum)).thenReturn(movieList);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies/pages/{pageNum}", pageNum)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();

        verify(myMovieService, times(1)).getMoviesPaging(pageNum);
    }
}