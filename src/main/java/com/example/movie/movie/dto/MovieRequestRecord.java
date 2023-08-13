package com.example.movie.movie.dto;

import jakarta.validation.constraints.Pattern;

public record MovieRequestRecord(
        Long releaseDate,
        String movieName,
        @Pattern(regexp = "[가-힣a-zA-Z0-9]*", message = "장르는 한글, 영어, 숫자로만 이루어져야 합니다.")
        String genre,
        String director,
        String postImageUrl,
        String originalTitle,
        Integer runningTime,
        String synopsis
) { }
