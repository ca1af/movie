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
import static com.example.movie.movie.entity.QCastMember.castMember;

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

    private BooleanExpression searchByGenre(String genre){
        return Objects.nonNull(genre) ? movie.genre.contains(genre) : null;
    }

    @Override
    public boolean existsByMovieId(Long movieId) {
        return jpaQueryFactory.selectOne()
                .from(movie)
                .where(movie.id.eq(movieId))
                .fetchFirst() != null;
        // fetchFirst == .limit(1).fetchOne()
    }

    @Override
    public boolean existsByMovieName(String movieName){
        return jpaQueryFactory.selectOne()
                .from(movie)
                .where(movie.movieName.eq(movieName))
                .fetchFirst() != null;
    }

    @Override
    public Optional<Movie> findByIdAndInUseIsTrue(Long movieId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(movie)
                        .leftJoin(movie.movieVideos, movieVideo).fetchJoin()
                        .leftJoin(movie.movieImages, movieImage).fetchJoin()
                        .leftJoin(movie.castMembers, castMember).fetchJoin()
                        .where(movie.inUse.eq(true),
                                movie.id.eq(movieId))
                        .fetchFirst()
        );
    }

    @Override
    public List<MovieResponseDto> getMoviesDefault() {

        List<Movie> movieList = jpaQueryFactory
                .selectFrom(movie)
                .leftJoin(movie.movieImages, movieImage).fetchJoin()
                .leftJoin(movie.movieVideos, movieVideo).fetchJoin()
                .leftJoin(movie.castMembers, castMember).fetchJoin()
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
                .leftJoin(movie.castMembers, castMember).fetchJoin()
                .where(movie.inUse.eq(true))
                .offset(offset)
                .limit(pageSize)
                .fetch();

        return MovieResponseDto.toDto(movieList);
    }


    @Override
    public List<MovieResponseDto> getMoviesBySearchCond(MovieSearchCond movieSearchCond) {

        if (movieSearchCond.getMovieName() == null && movieSearchCond.getDirector() == null && movieSearchCond.getGenre() == null){
            throw new IllegalArgumentException("항목을 입력 해 주세요");
        }

        List<Movie> movieList = jpaQueryFactory
                .selectFrom(movie)
                .leftJoin(movie.movieImages, movieImage).fetchJoin()
                .leftJoin(movie.movieVideos, movieVideo).fetchJoin()
                .leftJoin(movie.castMembers, castMember).fetchJoin()
                .where(
                        searchByMovieName(movieSearchCond.getMovieName()),
                        searchByDirector(movieSearchCond.getDirector()),
                        searchByGenre(movieSearchCond.getGenre()),
                        movie.inUse.eq(true))
                .fetch();

        return MovieResponseDto.toDto(movieList);
    }

    @Override
    public void deleteMovieById(Long movieId) {

        if (!existsByMovieId(movieId)){
            throw new IllegalArgumentException("해당하는 영화가 없습니다.");
        }

            jpaQueryFactory.delete(movieVideo)
                    .where(movieVideo.movie.id.eq(movieId))
                    .execute();

            jpaQueryFactory.delete(movieImage)
                    .where(movieImage.movie.id.eq(movieId))
                    .execute();

            jpaQueryFactory.delete(castMember)
                            .where(castMember.movie.id.eq(movieId))
                                    .execute();

            // Movie 엔티티 삭제
            jpaQueryFactory.delete(movie)
                    .where(movie.id.eq(movieId))
                    .execute();
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
