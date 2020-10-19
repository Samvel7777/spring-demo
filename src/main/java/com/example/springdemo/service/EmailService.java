package com.example.springdemo.service;

import com.example.springdemo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final MailSender mailSender;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void send(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        mailSender.send(simpleMailMessage);
    }

    @Async
    public void sendHtmlEmail(String to, String subject, User user, String link, String templateName, Locale locale) throws MessagingException {
        final Context ctx = new Context(locale);
        ctx.setVariable("user", user);
        ctx.setVariable("url", link);

        final String htmlContent = this.templateEngine.process(templateName, ctx);

        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setSubject(subject);
        message.setFrom("info@example.com");
        message.setTo(to);

        message.setText(htmlContent, true);

        this.javaMailSender.send(mimeMessage);
    }
}
