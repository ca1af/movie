package com.example.movie.movie.entity;

import com.example.movie.movie.dto.MovieRequestDto;
import com.example.movie.movie.dto.MovieRequestRecord;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long releaseDate;

    @Column(nullable = false, unique = true)
    private String movieName;

    private String originalTitle;

    private Integer runningTime;

    private String synopsis;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private String director;

    @Column
    private String posterImageUrl;

    private Boolean inUse = true;

//    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<MovieImage> movieImages = new LinkedHashSet<>();

//    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<MovieVideo> movieVideos = new LinkedHashSet<>();

//    @JsonIgnore
    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<CastMember> castMembers = new LinkedHashSet<>();

    @Builder
    public Movie(Long releaseDate, String movieName, String genre, String director, String posterImageUrl,
             String originalTitle, Integer runningTime, String synopsis) {
        this.releaseDate = releaseDate;
        this.movieName = movieName;
        this.genre = genre;
        this.director = director;
        this.posterImageUrl = posterImageUrl;
        this.originalTitle = originalTitle;
        this.runningTime = runningTime;
        this.synopsis = synopsis;
    }

    public void updateMovie(MovieRequestRecord movieRequestDto) {
        this.releaseDate = movieRequestDto.releaseDate();
        this.movieName = movieRequestDto.movieName();
        this.genre = movieRequestDto.genre();
        this.director = movieRequestDto.director();
        this.posterImageUrl = movieRequestDto.postImageUrl();
    }

    public void softDeleteMovie(){
        this.inUse = false;
    }
}
