package com.example.movie.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
