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
        message.setTo("fco.20mochi@gmailcom");
        message.setFrom("mindBanc@outlook.com");
        message.setSubject("Mensaje de Prueba Banco MindBanc");
        message.setText("Mensaje de prueba no responder");

        mailSender.send(message);
    }
}