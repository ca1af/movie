package com.example.movie.common.batch;

import com.example.movie.common.mail.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JobCaller {
    private final JobLauncher jobLauncher;
    private final Job simpleJob1; // bean으로 등록되어 있기 때문에 의존 주입 받을 수 있다.
    private final MailService mailService;
    @Value("${output.file.path}")
    private String outputFilePath;

    @Scheduled(cron = "0 * * * * *")
    public void runApiCallJob() throws JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException, MessagingException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters();

        jobLauncher.run(simpleJob1, jobParameters);

        // 파일 생성 완료 후 이메일 발송
        String recipientEmail = "laancer@naver.com";
        String subject = "테스트 1";
        String body = "테스트 1번입니다.";

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        String formattedDate = currentDate.format(formatter);

        String filePath = outputFilePath + "/movie_" + formattedDate + ".txt";
        mailService.sendEmailWithAttachment(recipientEmail, subject, body, filePath);
    }
}
