package com.example.movie.common.batch;

import com.example.movie.common.aop.AnnotationBasedAOP;
import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@EnableScheduling
@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfig {
    private final MovieRepository movieRepository;
    @Bean
    public Job simpleJob1(JobRepository jobRepository, Step simpleStep1, Step simpleStep2) {
        return new JobBuilder("simpleJob", jobRepository)
                .start(simpleStep1)
                .next(simpleStep2)
                .build();
    }
    @Bean
    public Step simpleStep1(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet(testTasklet, platformTransactionManager).build();
    }
    @Bean
    public Tasklet testTasklet(){
        return ((contribution, chunkContext) -> {
            ArrayList<Long> ids = new ArrayList<>(AnnotationBasedAOP.map.keySet());
            HashMap<String, Long> names = new HashMap<>();

            List<Movie> movieList = movieRepository.findAllById(ids);

            for (Movie movie : movieList) {
                names.put(movie.getMovieName(), AnnotationBasedAOP.map.get(movie.getId()));
            }

            // TODO: 2023/06/12 이곳에 movieNameMap을 파일/디비로 저장하는 로직이 필요하다.
            //  현재는 로깅하도록 하자

            for (String s : names.keySet()) {
                log.info("영화 이름 :" + s + ", 호출 횟수 :" + names.get(s));
            }

//            AnnotationBasedAOP.map.clear();

            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    public Step simpleStep2(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep2", jobRepository)
                .tasklet(testTasklet2(), platformTransactionManager).build();
    }

    @Bean
    public Tasklet testTasklet2(){
        return ((contribution, chunkContext) -> {

            AnnotationBasedAOP.map.clear();
            // 클리어 해주기
            return RepeatStatus.FINISHED;
        });
    }

}
