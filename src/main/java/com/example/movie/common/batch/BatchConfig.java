package com.example.movie.common.batch;

import com.example.movie.common.aop.AnnotationBasedAOP;
import com.example.movie.movie.entity.Movie;
import com.example.movie.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EnableScheduling
@Configuration
@Slf4j
@RequiredArgsConstructor
public class BatchConfig {
    private final MovieRepository movieRepository;

    @Value("${output.file.path}")
    private String outputFilePath;

    @Bean
    public Job movieCountJob(JobRepository jobRepository, Step countStep, Step clearStep) {
        return new JobBuilder("simpleJob", jobRepository)
                .start(countStep)
                .next(clearStep)
                .build();
    }

    @Bean
    public Step countStep(JobRepository jobRepository, Tasklet countTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("simpleStep1", jobRepository)
                .tasklet(countTasklet, platformTransactionManager).build();
    }

    @Bean
    public Tasklet countTasklet() {
        return ((contribution, chunkContext) -> {
            ArrayList<Long> ids = new ArrayList<>(AnnotationBasedAOP.map.keySet());
            HashMap<String, Long> names = new HashMap<>();

            List<Movie> movieList = movieRepository.findAllById(ids);

            for (Movie movie : movieList) {
                names.put(movie.getMovieName(), AnnotationBasedAOP.map.get(movie.getId()));
            }

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
            String formattedDate = currentDate.format(formatter);

            String fileName = "movie_" + formattedDate + ".txt";
            String filePath = outputFilePath + "/" + fileName;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String s : names.keySet()) {
                    String line = "영화 이름: " + s + ", 호출 횟수: " + names.get(s);
                    writer.write(line);
                    writer.newLine();
                }
                log.info("데이터를 파일에 저장했습니다: " + outputFilePath);
            } catch (IOException e) {
                log.error("파일 저장 중 오류가 발생했습니다: " + outputFilePath, e);
            }

            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    public Step clearStep(JobRepository jobRepository, Tasklet clearTasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("simpleStep2", jobRepository)
                .tasklet(clearTasklet(), platformTransactionManager).build();
    }

    @Bean
    public Tasklet clearTasklet() {
        return ((contribution, chunkContext) -> {

            AnnotationBasedAOP.map.clear();
            // 클리어 해주기
            return RepeatStatus.FINISHED;
        });
    }

}
