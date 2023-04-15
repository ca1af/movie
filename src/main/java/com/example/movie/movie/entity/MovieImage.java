package com.example.movie.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MovieImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    @Builder
    public MovieImage(Long id, String imageUrl, Movie movie) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.movie = movie;
    }
}
