package com.example.servicebot2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender {

    @Value("${spring.mail.username}")
    private String username;

    @Autowired
    private JavaMailSender mailSender;

    public void send(String emailTo,String subject,String message)throws MailSendException{

        SimpleMailMessage mailMessage=new SimpleMailMessage();

        mailMessage.setFrom(username);//Без корректного заполнение этого поля почтовый сервер может не принять сообщение
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

//        try{
//            mailSender.send(mailMessage);//Отправляем сформированное сообщение
//        }catch (MailSendException ex){
//            throw new MailSendException("Неверное имя Email");
//        }

        mailSender.send(mailMessage);//Отправляем сформированное сообщение


    }
}
