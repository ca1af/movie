package com.example.movie.common.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JobCaller {
    private final JobLauncher jobLauncher;
    private final Job simpleJob1; // bean으로 등록되어 있기 때문에 의존 주입 받을 수 있다.
    @Scheduled(cron = "0 0 4 * * *")
    public void runApiCallJob() throws JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters();

        jobLauncher.run(simpleJob1, jobParameters);
    }
}
