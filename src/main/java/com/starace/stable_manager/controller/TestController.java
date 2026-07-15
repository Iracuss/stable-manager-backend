package com.starace.stable_manager.controller;

import org.springframework.web.bind.annotation.RestController;

import com.starace.stable_manager.scheduler.HorseNotificationScheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Just testing, will delete class once tests are done

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final HorseNotificationScheduler scheduler;

    @GetMapping("/email-trigger")
    public String triggerEmailsNow() {
        scheduler.dailyOverdueHorseCheck();
        return "Emails triggered!";
    }
    
}
