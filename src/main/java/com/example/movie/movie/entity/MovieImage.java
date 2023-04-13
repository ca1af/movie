package com.example.movie.movie.entity;

import jakarta.persistence.*;

@Entity
public class MovieImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
}
