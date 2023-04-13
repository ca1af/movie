package com.example.movie.movie.entity;

import com.example.movie.movie.dto.MovieRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

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

    private Boolean inUse = true;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<MovieImage> movieImages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<MovieVideo> movieVideos = new LinkedHashSet<>();

    @Builder
    public Movie(Long releaseDate, String movieName, String genre, String director, String posterImageUrl) {
        this.releaseDate = releaseDate;
        this.movieName = movieName;
        this.genre = genre;
        this.director = director;
        this.posterImageUrl = posterImageUrl;
    }

    public void updateMovie(MovieRequestDto movieRequestDto) {
        this.releaseDate = movieRequestDto.getReleaseDate();
        this.movieName = movieRequestDto.getMovieName();
        this.genre = movieRequestDto.getGenre();
        this.director = movieRequestDto.getDirector();
        this.posterImageUrl = movieRequestDto.getPostImageUrl();
    }

    public void softDeleteMovie(Movie movie){
        movie.inUse = false;
    }
}
