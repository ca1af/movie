package com.example.movie.movie.repository;

import com.example.movie.movie.entity.CastMember;
import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.entity.MovieImage;
import com.example.movie.movie.entity.MovieVideo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcBulkInsertRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcBulkInsertRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void bulkInsertMovies(List<Movie> movies) {
        String sql = "INSERT INTO movie (release_date, movie_name, original_title, running_time, synopsis, genre, director, poster_image_url, in_use) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Movie movie = movies.get(i);
                ps.setLong(1, movie.getReleaseDate());
                ps.setString(2, movie.getMovieName());
                ps.setString(3, movie.getOriginalTitle());
                ps.setInt(4, movie.getRunningTime());
                ps.setString(5, movie.getSynopsis());
                ps.setString(6, movie.getGenre());
                ps.setString(7, movie.getDirector());
                ps.setString(8, movie.getPosterImageUrl());
                ps.setBoolean(9, movie.getInUse());
            }

            @Override
            public int getBatchSize() {
                return movies.size();
            }
        });
    }


    public void bulkInsertMovieImage(List<MovieImage> movieImages) {
        String movieImageSql = "INSERT INTO movie_image (image_url, movie_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(movieImageSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MovieImage movieImage = movieImages.get(i);
                ps.setString(1, movieImage.getImageUrl());
                ps.setLong(2, movieImage.getMovie().getId());
            }

            @Override
            public int getBatchSize() {
                return movieImages.size();
            }
        });
    }

    public void bulkInsertMovieVideo(List<MovieVideo> movieVideos) {
        String movieVideoSql = "INSERT INTO movie_video (video_url, movie_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(movieVideoSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MovieVideo movieVideo = movieVideos.get(i);
                ps.setString(1, movieVideo.getVideoUrl());
                ps.setLong(2, movieVideo.getMovie().getId());
            }

            @Override
            public int getBatchSize() {
                return movieVideos.size();
            }
        });
    }

    public void bulkInsertCastMember(List<CastMember> castMembers) {
        String castMemberSql = "INSERT INTO cast_member (member_name, movie_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(castMemberSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CastMember castMember = castMembers.get(i);
                ps.setString(1, castMember.getMemberName());
                ps.setLong(2, castMember.getMovie().getId());
            }

            @Override
            public int getBatchSize() {
                return castMembers.size();
            }
        });

    }
}

