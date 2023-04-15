package com.example.movie.movie.repository.query;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.entity.Movie;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.movie.movie.entity.QMovie.movie;
import static com.example.movie.movie.entity.QMovieImage.movieImage;
import static com.example.movie.movie.entity.QMovieVideo.movieVideo;

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
    public List<MovieResponseDto> getMoviesDefault() {
        List<Movie> movieList = jpaQueryFactory
                .selectFrom(movie)
                .innerJoin(movie.movieImages, movieImage).fetchJoin()
                .innerJoin(movie.movieVideos, movieVideo).fetchJoin()
                .where(movie.inUse.eq(true))
                .fetch();

        return movieList.stream().map(MovieResponseDto::of).collect(Collectors.toList());
    }

    @Override
    public List<MovieResponseDto> getMovies(Long pageNum) {
        int pageSize = 10;
        long offset = (pageNum - 1) * pageSize;

        List<Movie> movieList = jpaQueryFactory
                .selectFrom(movie)
                .innerJoin(movie.movieImages, movieImage).fetchJoin()
                .innerJoin(movie.movieVideos, movieVideo).fetchJoin()
                .where(movie.inUse.eq(true))
                .offset(offset)
                .limit(pageSize)
                .fetch();

        return movieList.stream().map(MovieResponseDto::of).collect(Collectors.toList());
    }

    @Override
    public List<MovieResponseDto> searchMovieByCond(MovieSearchCond movieSearchCond) {
        List<Movie> movieList = jpaQueryFactory
                .selectFrom(movie)
                .innerJoin(movie.movieImages, movieImage).fetchJoin()
                .innerJoin(movie.movieVideos, movieVideo).fetchJoin()
                .where(
                        searchByMovieName(movieSearchCond.getMovieName()),
                        searchByDirector(movieSearchCond.getDirector()),
                        movie.inUse.eq(true))
                .fetch();

        return movieList.stream().map(MovieResponseDto::of).collect(Collectors.toList());
    }


    @Override
    public Optional<Movie> findByIdAndInUseIsTrue(Long movieId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(movie)
                        .innerJoin(movie.movieVideos, movieVideo).fetchJoin()
                        .innerJoin(movie.movieImages, movieImage).fetchJoin()
                        .where(movie.inUse.eq(true),
                                movie.id.eq(movieId))
                        .fetchOne()
        );
    }

    @Override
    public void deleteMovieById(Long movieId) {
        // join 이 일어나지 않는 가벼운 쿼리로 객체 탐색
        Movie foundMovie = jpaQueryFactory.selectFrom(movie)
                .where(movie.id.eq(movieId))
                .fetchOne();
        // N+1 delete 를 막기 위해. Native query 로 한번에 가능하지만, 위험하므로 아래와 같이 작성
        if (foundMovie != null) {
            jpaQueryFactory.delete(movieVideo)
                    .where(movieVideo.movie.eq(movie))
                    .execute();

            jpaQueryFactory.delete(movieImage)
                    .where(movieImage.movie.eq(movie))
                    .execute();

            // Movie 엔티티 삭제
            jpaQueryFactory.delete(movie)
                    .where(movie.id.eq(movieId))
                    .execute();
        }
    }
    @Override
    public void softDeleteMovieById(Long movieId) {
        jpaQueryFactory
                .update(movie)
                .set(movie.inUse, false)
                .where(movie.id.eq(movieId))
                .execute();
    }
}
