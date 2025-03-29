package com.manhnv.hexatask.service;

import com.manhnv.hexatask.utils.Constants;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender emailSender;

    @Async
    public void sendEmail(String to, String token) {
        try {
            Context context = new Context();
            context.setVariable("token", token);
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(to);
            helper.setSubject(Constants.SUBJECT_MAIL);
            String htmlContent = templateEngine.process(Constants.EMAIL_TEMPLATE, context);
            helper.setText(htmlContent, true);

            emailSender.send(mimeMessage);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
