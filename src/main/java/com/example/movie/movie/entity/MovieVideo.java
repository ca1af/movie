package com.example.movie.movie.entity;

import jakarta.persistence.*;
@Entity
public class MovieVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
}
