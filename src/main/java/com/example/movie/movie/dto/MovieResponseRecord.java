package com.example.movie.movie.dto;

import com.example.movie.movie.entity.Movie;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record MovieResponseRecord(
        Long id,
        Long releaseDate,
        String movieName,
        String genre,
        String director,
        String postImageUrl,
        Set<MovieImageResponseDto> movieImages,
        Set<MovieVideoResponseDto> movieVideos,
        Set<CastMemberResponseDto> castMembers
) {
    private MovieResponseRecord(Movie movie){
        this(
                movie.getId(),
                movie.getReleaseDate(),
                movie.getMovieName(),
                movie.getGenre(),
                movie.getMovieName(),
                movie.getPosterImageUrl(),
                movie.getMovieImages().stream()
                        .map(MovieImageResponseDto::of)
                        .sorted(Comparator.comparingLong(MovieImageResponseDto::getId))
                        // 출력 순서를 id 순으로 보장하는 로직
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                movie.getMovieVideos().stream()
                        .map(MovieVideoResponseDto::of)
                        .sorted(Comparator.comparingLong(MovieVideoResponseDto::getId))
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                movie.getCastMembers().stream()
                        .map(CastMemberResponseDto::of)
                        .sorted(Comparator.comparingLong(CastMemberResponseDto::getId))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public static MovieResponseRecord of(Movie movie){
        return new MovieResponseRecord(movie);
    }

    public static List<MovieResponseRecord> toDtoRecord(List<Movie> movies){
        return movies.stream().map(MovieResponseRecord::of).collect(Collectors.toList());
    }
}
