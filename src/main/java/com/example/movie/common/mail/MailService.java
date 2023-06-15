package com.example.movie.common.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender mailSender;
    public void sendEmailWithAttachment(String receiverEmail, String subject, String body, String filePath)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(receiverEmail);
        helper.setSubject(subject);
        helper.setText(body);

        File attachment = new File(filePath); // 패스에 이미 저장해놓은 텍스트 파일임 ( 먼저 실행된 Job에서 생성됨 )
        helper.addAttachment(attachment.getName(), attachment);

        mailSender.send(message);
    }
}
