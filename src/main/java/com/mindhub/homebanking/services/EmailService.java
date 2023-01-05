package com.mindhub.homebanking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body)throws MessagingException
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom("mindBanc@outlook.com");
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}