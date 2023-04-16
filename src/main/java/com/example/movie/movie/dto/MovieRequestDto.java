package com.example.movie.movie.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.*;

@Getter
@NoArgsConstructor
public class MovieRequestDto {
    private Long releaseDate;
    private String movieName;
//    @Pattern(regexp = "[가-힣a-zA-Z0-9]*", message = "genre는 한글, 영어, 숫자로만 이루어져야 합니다.")
    private String genre;
    private String director;
    private String postImageUrl;
}
