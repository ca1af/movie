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

        String receiverEmail = "laancer@naver.com"; // 받는 사람 이메일 주소
        String subject = "테스트 : 제목"; // 제목
        String body = "테스트를 위해서 발송됩니다"; // 내용

        // 파일 만들 때 시간 저장하던 놈들. 시간을 찍어주기 위해서 사용한다
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        String formattedDate = currentDate.format(formatter);

        String filePath = outputFilePath + "/movie_" + formattedDate + ".txt"; // 파일 경로
        mailService.sendEmailWithAttachment(receiverEmail, subject, body, filePath);
    }
}
