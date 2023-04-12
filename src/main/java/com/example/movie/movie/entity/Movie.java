package com.example.movie.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private Long releaseDate;
    @Column(nullable = false)
    private String movieName;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private String director;
    @Column
    private String posterImageUrl;
    @Builder
    public Movie(Long releaseDate, String movieName, String genre, String director, String posterImageUrl) {
        this.releaseDate = releaseDate;
        this.movieName = movieName;
        this.genre = genre;
        this.director = director;
        this.posterImageUrl = posterImageUrl;
    }
}
