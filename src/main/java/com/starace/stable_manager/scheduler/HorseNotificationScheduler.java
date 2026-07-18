package com.starace.stable_manager.scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.starace.stable_manager.dto.HorseAlert;
import com.starace.stable_manager.enums.AlertType;
import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.repository.HorseRepository;
import com.starace.stable_manager.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HorseNotificationScheduler {
    // public final HorseService horseService;
    public final HorseRepository horseRepository;
    public final EmailService emailService;

    @Scheduled(cron = "@midnight")
    public void dailyOverdueHorseCheck() {
        List<Horse> allHorses = horseRepository.findAll();
        Map<String, List<HorseAlert>> alertsByEmail = new HashMap<>();

        for(Horse horse : allHorses) {
            List<AlertType> alertsForHorse = checkAlert(horse);

            if(!alertsForHorse.isEmpty()) {
                String userEmail = ""; //horse.getUser().getEmail(); have to leave it like this for now
                String horseName = horse.getName();

                HorseAlert horseAlert = new HorseAlert();
                horseAlert.setHorseName(horseName);
                horseAlert.setAlerts(alertsForHorse);

                alertsByEmail
                    .computeIfAbsent(userEmail, k -> new ArrayList<>())
                    .add(horseAlert);            
            }
        }

        for(Map.Entry<String, List<HorseAlert>> entry : alertsByEmail.entrySet()) {
            String email = entry.getKey();
            List<HorseAlert> alerts = entry.getValue();
            emailService.sendOverdueEmail(email, alerts);
        }
    }

    private List<AlertType> checkAlert(Horse horse) {
        List<AlertType> alerts = new ArrayList<>();

        if(isCogginsOverdue(horse)) {
            alerts.add(AlertType.COGGINS);
        }
        if(isFarrierOverdue(horse)) {
            alerts.add(AlertType.FARRIER);
        }
        return alerts;
    }

    private boolean isCogginsOverdue(Horse horse) {
        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.minusYears(1);

        if(horse.getLastCogginDate() == null) {
            return false;
        } else {
            return horse.getLastCogginDate().isBefore(cutoff);
        }
    }

    private boolean isFarrierOverdue(Horse horse) {
        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.minusWeeks(6);

        if(horse.getLastFarrierDate() == null) {
            return false;
        } else {
            return horse.getLastFarrierDate().isBefore(cutoff);
        }
    }

}
