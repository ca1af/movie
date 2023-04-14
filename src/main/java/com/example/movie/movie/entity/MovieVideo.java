package com.example.movie.movie.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
public class MovieVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
    @Builder
    public MovieVideo(Long id, String videoUrl, Movie movie) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.movie = movie;
    }

    public void updateVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
