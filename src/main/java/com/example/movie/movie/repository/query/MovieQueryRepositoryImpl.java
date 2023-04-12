package com.example.movie.movie.repository.query;

import com.example.movie.movie.dto.MovieResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.example.movie.movie.entity.QMovie.movie;

@Repository
public class MovieQueryRepositoryImpl implements MovieQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public MovieQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private BooleanExpression searchByMovieName(String movieName){
        return Objects.nonNull(movieName) ? movie.movieName.contains(movieName) : null;
    }

    private BooleanExpression searchByDirector(String director){
        return Objects.nonNull(director) ? movie.director.contains(director) : null;
    }

    @Override
    public List<MovieResponseDto> searchMovieByCond(MovieSearchCond movieSearchCond) {
//        jpaQueryFactory.select()
        return null;
    }
}
