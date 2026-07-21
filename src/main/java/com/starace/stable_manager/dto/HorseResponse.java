package com.starace.stable_manager.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class HorseResponse {
    private Long id;
    private String name;
    private String breed;
    private Integer birthYear;
    private String nickname;
    private String microchipId;
    private Boolean isMdBred;
    private String foalingState;
    private LocalDate lastCogginDate;
    private LocalDate lastFarrierDate;
    private String medicalNotes;
    private Long stableId;
}
