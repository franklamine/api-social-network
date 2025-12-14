package com.frank.apisocialnetwork.service;

import com.frank.apisocialnetwork.entity.Validation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Slf4j
@AllArgsConstructor
@Service
public class NotificationService {

    JavaMailSender javaMailSender;
    public void envoyerNotification(Validation validation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@frank.tech");
        message.setTo(validation.getUtilisateur().getEmail());
        message.setSubject("Code de vérification");
        message.setText("Bonjour votre code de vérification est: " + validation.getCode());

        javaMailSender.send(message);
        log.info("mail envoyé a " + validation.getUtilisateur().getEmail());
    }
}
