package com.example.movie.movie.repository.query;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.dto.QMovieResponseDto;
import com.example.movie.movie.entity.Movie;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.movie.movie.entity.QMovie.movie;

@Repository
public class MovieQueryRepositoryImpl implements MovieQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public MovieQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    private BooleanExpression searchByMovieName(String movieName) {
        return Objects.nonNull(movieName) ? movie.movieName.contains(movieName) : null;
    }

    private BooleanExpression searchByDirector(String director) {
        return Objects.nonNull(director) ? movie.director.contains(director) : null;
    }

    @Override
    public List<MovieResponseDto> getMovieList(Long pageNum) {
        return jpaQueryFactory
                .select(new QMovieResponseDto(movie.id, movie.releaseDate, movie.movieName, movie.genre, movie.director, movie.posterImageUrl))
                .from(movie)
                .where(movie.inUse.eq(true))
                .offset(pageNum - 1)
                .limit(pageNum - 1 + 10)
                .fetch();
    }

    @Override
    public List<MovieResponseDto> searchMovieByCond(MovieSearchCond movieSearchCond) {
        return jpaQueryFactory
                .select((new QMovieResponseDto(movie.id, movie.releaseDate, movie.movieName, movie.genre, movie.director, movie.posterImageUrl)))
                .from(movie)
                .where(
                        searchByMovieName(movieSearchCond.getMovieName()),
                        searchByDirector(movieSearchCond.getDirector()),
                        movie.inUse.eq(true))
                .fetch();
    }

    @Override
    public Optional<Movie> findByIdAndInUseIsTrue(Long movieId) {
        return Optional.ofNullable(
                jpaQueryFactory
                .selectFrom(movie)
                .where(movie.inUse.eq(true),
                        movie.id.eq(movieId))
                .fetchOne()
        );
    }

    @Override
    public void deleteMovieById(Long movieId) {
        jpaQueryFactory
                .delete(movie)
                .where(movie.id.eq(movieId))
                .execute();
    }
//
//    public void softDeleteMovieById(Long movieId) {
//        jpaQueryFactory
//                .update(movie)
//                .set(movie.inUse, false)
//                .where(movie.id.eq(movieId))
//                .execute();
//    }
}
