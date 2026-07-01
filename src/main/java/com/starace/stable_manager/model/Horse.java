package com.starace.stable_manager.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Creates the getters and setters and toString
@NoArgsConstructor // Creates an empty constructor since its required
@AllArgsConstructor // Makes a constructor with every arg we put
public class Horse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String breed;
    private Integer birthYear;
    private String nickname;

    private String microchipId; // if anything we can change to registration for tattoos as well

    private Boolean isMdBred;
    private String foalingState; // Where was the horse born, technically not needed but nice to have and track
    private LocalDate lastCogginDate; // Need to check every 12 months
    private LocalDate lastFarrierDate; // Need to check every 6 weeks since its standard

    private String medicalNotes; // Just in case

}
