package com.example.movie.common.batch;

import com.example.movie.common.aop.ExecutionTimer;
import com.example.movie.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

@EnableBatchProcessing
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
                .start(simpleStep2)
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
            HashMap<String, Long> movieNameMap = new HashMap<>();

            for (String s : ExecutionTimer.map.keySet()) {
                String movieName = movieRepository.findById(Long.parseLong(s)).orElseThrow().getMovieName();
                movieNameMap.put(movieName, ExecutionTimer.map.get(s));

                // TODO: 2023/06/12 이곳에 movieNameMap을 파일/디비로 저장하는 로직이 필요하다.
                //  현재는 로깅하도록 하자

                log.info("영화 이름 :" + movieName + ", 호출 횟수 :" + ExecutionTimer.map.get(s));
            }

            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    public Step simpleStep2(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager){
        return new StepBuilder("simpleStep2", jobRepository)
                .tasklet(testTasklet, platformTransactionManager).build();
    }

    @Bean
    public Tasklet testTasklet2(){
        return ((contribution, chunkContext) -> {

            ExecutionTimer.map.clear();
            // 클리어 해주기
            return RepeatStatus.FINISHED;
        });
    }

}
