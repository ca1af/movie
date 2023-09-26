package com.example.movie.movie.controller;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.repository.query.MovieSearchCond;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MyMovieControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetMoviesPagingSuccess() {
        Long pageNum = 1L;
        ResponseEntity<MovieResponseDto[]> responseEntity = restTemplate.getForEntity("/movies/pages/{pageNum}", MovieResponseDto[].class, pageNum);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testMoviesBySearchCondFailure() {
        MovieSearchCond invalidSearchCond = MovieSearchCond.builder()
                .movieName("Invalid Movie Name!@#$") // Invalid movie name
                .director("Sample Director")
                .genre("Action")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MovieSearchCond> requestEntity = new HttpEntity<>(invalidSearchCond, headers);

        ResponseEntity<Void> responseEntity = restTemplate.exchange("/movies/search", HttpMethod.GET, requestEntity, Void.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
