package com.example.movie.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
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
}
