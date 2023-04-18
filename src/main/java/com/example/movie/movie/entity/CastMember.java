package com.example.movie.movie.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CastMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String memberName;
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
    @Builder
    public CastMember(String memberName, Movie movie) {
        this.memberName = memberName;
        this.movie = movie;
    }
}
