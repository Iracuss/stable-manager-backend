package com.starace.stable_manager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.HorseAlert;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Async
    public void sendOverdueEmail(String to, List<HorseAlert> alertMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        String textMessage = formatOverdueEmail(alertMessage);

        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject("You have horses overdue for appointments");
        message.setText(textMessage);

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String resetLink) {

    }

    private String formatOverdueEmail(List<HorseAlert> alertMessage) {
        StringBuilder formattedMessage = new StringBuilder();

        alertMessage.forEach(alert -> {
            formattedMessage
                .append(alert.getHorseName()).append(" is overdue for: ");

            String formattedAlerts = alert.getAlerts().stream()
                .map(type -> type.name())
                .collect(Collectors.joining(", "));

            formattedMessage.append(formattedAlerts).append('\n');
        });

        return formattedMessage.toString();
    }
}
