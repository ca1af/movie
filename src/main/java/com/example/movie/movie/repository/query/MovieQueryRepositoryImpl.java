package com.example.movie.movie.repository.query;

import com.example.movie.movie.dto.MovieResponseDto;
import com.example.movie.movie.entity.Movie;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public boolean existsByMovieName(String movieName){
        return jpaQueryFactory.selectOne()
                .from(movie)
                .where(movie.movieName.eq(movieName))
                .fetchOne() != null;
    }

    @Override
    public Optional<Movie> findByIdAndInUseIsTrue(Long movieId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(movie)
                        .leftJoin(movie.movieVideos, movieVideo).fetchJoin()
                        .leftJoin(movie.movieImages, movieImage).fetchJoin()
                        .where(movie.inUse.eq(true),
                                movie.id.eq(movieId))
                        .fetchOne()
        );
    }

    @Override
    public List<MovieResponseDto> getMoviesDefault() {

        List<Movie> movieList = jpaQueryFactory
                .selectFrom(movie)
                .leftJoin(movie.movieImages, movieImage).fetchJoin()
                .leftJoin(movie.movieVideos, movieVideo).fetchJoin()
                .where(movie.inUse.eq(true))
                .fetch();

        return MovieResponseDto.toDto(movieList);
    }

    @Override
    public List<MovieResponseDto> getMoviesPaging(Long pageNum) {

        int pageSize = 10;

        pageNum = Math.max(pageNum, 1L);

        long offset = (pageNum - 1) * pageSize;

        offset = Math.max(offset, 0L);

        Integer one = jpaQueryFactory.selectOne()
                .from(movie)
                .where(movie.id.between(offset, offset + 10))
                .fetchOne();

        List<MovieResponseDto> nullPointHandler = new ArrayList<>();

        if (one == null){
            return nullPointHandler;
        }

        List<Movie> movieList = jpaQueryFactory
                .selectFrom(movie)
                .leftJoin(movie.movieImages, movieImage).fetchJoin()
                .leftJoin(movie.movieVideos, movieVideo).fetchJoin()
                .where(movie.inUse.eq(true))
                .offset(offset)
                .limit(pageSize)
                .fetch();

        return MovieResponseDto.toDto(movieList);
    }


    @Override
    public List<MovieResponseDto> searchMovieByCond(MovieSearchCond movieSearchCond) {
        List<Movie> movieList = jpaQueryFactory
                .selectFrom(movie)
                .leftJoin(movie.movieImages, movieImage).fetchJoin()
                .leftJoin(movie.movieVideos, movieVideo).fetchJoin()
                .where(
                        searchByMovieName(movieSearchCond.getMovieName()),
                        searchByDirector(movieSearchCond.getDirector()),
                        movie.inUse.eq(true))
                .fetch();

        return MovieResponseDto.toDto(movieList);
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
//    @Override
//    public void softDeleteMovieById(Long movieId) {
//        jpaQueryFactory
//                .update(movie)
//                .set(movie.inUse, false)
//                .where(movie.id.eq(movieId))
//                .execute();
//    }
}
