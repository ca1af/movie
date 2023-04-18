package com.example.movie.movie.dto;

import com.example.movie.movie.entity.CastMember;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CastMemberResponseDto {
    private Long id;
    private Long movieId;
    private String memberName;

    private CastMemberResponseDto(CastMember castMember) {
        this.id = castMember.getId();
        this.movieId = castMember.getMovie().getId();
        this.memberName = castMember.getMemberName();
    }

    public static CastMemberResponseDto of(CastMember castMember){
        return new CastMemberResponseDto(castMember);
    }
}
