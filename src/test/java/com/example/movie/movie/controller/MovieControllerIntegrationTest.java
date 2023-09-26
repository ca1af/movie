package com.example.movie.movie.controller;

import com.example.movie.movie.dto.MovieRequestRecord;
import com.example.movie.movie.dto.MovieResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetMoviesDefaultSuccess() {
        ResponseEntity<MovieResponseDto[]> responseEntity = restTemplate.getForEntity("/api/v1/movies", MovieResponseDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        MovieResponseDto[] body = responseEntity.getBody();
        MovieResponseDto movieResponseDto = body[0];
        assertNotNull(movieResponseDto.getMovieVideos());
    }

    @Test
    void testGetMovieByIdSuccess() {
        Long movieId = 1L;
        ResponseEntity<MovieResponseDto> responseEntity = restTemplate.getForEntity("/api/v1/movies/{movieId}", MovieResponseDto.class, movieId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testCreateMovieSuccess() {
        String movieName = "Test Movie" + System.currentTimeMillis();
        MovieRequestRecord movieRequestDto = new MovieRequestRecord(
                2023L,
                movieName.substring(0, 13), // 중복된 이름을 막기 위함
                "Action1",
                "Test Director",
                "http://example.com/poster.jpg",
                "Original Title",
                120,
                "Test Synopsis"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MovieRequestRecord> requestEntity = new HttpEntity<>(movieRequestDto, headers);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("/api/v1/movies", requestEntity, Void.class);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateMovieSuccess() {
        String movieName = "Test Movie" + System.currentTimeMillis();
        Long movieId = 1L;
        MovieRequestRecord movieRequestDto = new MovieRequestRecord(
                2023L,
                movieName.substring(0, 11), // 중복된 이름을 막기 위함
                "Action1",
                "Test Director",
                "http://example.com/poster.jpg",
                "Original Title",
                120,
                "Test Synopsis"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MovieRequestRecord> requestEntity = new HttpEntity<>(movieRequestDto, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/api/v1/movies/{movieId}", HttpMethod.PUT, requestEntity, Void.class, movieId); // 수정된 부분
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteMovieSuccess() {
        Long movieId = 1L;
        ResponseEntity<Void> responseEntity = restTemplate.exchange("/api/v1/movies/{movieId}", HttpMethod.DELETE, null, Void.class, movieId);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
