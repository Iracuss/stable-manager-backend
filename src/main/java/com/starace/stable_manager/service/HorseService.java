package com.starace.stable_manager.service;

// import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.HorseRequest;
import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.model.User;
import com.starace.stable_manager.repository.HorseRepository;
import com.starace.stable_manager.repository.StableRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

// Need to fix this but this is last

@Service
@RequiredArgsConstructor
public class HorseService {
    private final HorseRepository horseRepository;
    private final StableRepository stableRepository;
    private final CurrentUserService currentUserService;

    public Horse saveHorse(HorseRequest request) {


        Horse horse = new Horse();
        horse.setName(request.getName());
        horse.setBreed(request.getBreed());
        horse.setBirthYear(request.getBirthYear());
        horse.setNickname(request.getNickname());
        horse.setMicrochipId(request.getMicrochipId());
        horse.setIsMdBred(request.getIsMdBred());
        horse.setFoalingState(request.getFoalingState());
        horse.setLastCogginDate(request.getLastCogginDate());
        horse.setLastFarrierDate(request.getLastFarrierDate());
        horse.setMedicalNotes(request.getMedicalNotes());
        // horse.setUser(getCurrentUser());
        // horse.setStable(horse);
        // get and set the stable its in
        // checkHealthAlerts(horse);
        return horseRepository.save(horse);
    }

    public List<Horse> getAllHorses() {
        User currentUser = currentUserService.getCurrentUser();
        List<Horse> horses = horseRepository.findByUserId(currentUser.getId());

        // horses.forEach(this::checkHealthAlerts); // check each horses need for something
        return horses;
    }

    public Horse getHorseById(Long id) {
        User currentUser = currentUserService.getCurrentUser();
        return horseRepository.findByIdAndUserId(id, currentUser.getId())
            .orElseThrow(() -> new EntityNotFoundException("Horse not found with id: " + id));
    }

    // public void checkHealthAlerts(Horse horse) {
    //     if(horse.getLastCogginDate() != null) {
    //         if(horse.getLastCogginDate().isBefore(LocalDate.now().minusYears(1))) {
    //             System.out.println("LEGAL ALERT: " + horse.getName() + " has an EXPIRED Coggins test!");
    //         }
    //     }

    //     if(horse.getLastFarrierDate() != null) {
    //         if(horse.getLastFarrierDate().isBefore(LocalDate.now().minusWeeks(6))) {
    //             System.out.println("MAINTENANCE ALERT: " + horse.getName() + " is overdue for the Farrier!");
    //         }
    //     }
    // }

    // public int calculateRacingAge(Horse horse) {
    //     if (horse.getBirthYear() == null) return 0;
        
    //     int currentYear = LocalDate.now().getYear();
    //     // Racing age formula
    //     return currentYear - horse.getBirthYear();
    // }

    public Horse updateHorse(Long id, HorseRequest horseDetails) {
        User currentUser = currentUserService.getCurrentUser();
        return horseRepository.findByIdAndUserId(id, currentUser.getId()).map(horse -> {
            if(horseDetails.getName() != null) horse.setName(horseDetails.getName());
            if(horseDetails.getBreed() != null) horse.setBreed(horseDetails.getBreed());
            if(horseDetails.getBirthYear() != null) horse.setBirthYear(horseDetails.getBirthYear());
            if(horseDetails.getNickname() != null) horse.setNickname(horseDetails.getNickname());
            if(horseDetails.getMicrochipId() != null) horse.setMicrochipId(horseDetails.getMicrochipId());
            if(horseDetails.getIsMdBred() != null) horse.setIsMdBred(horseDetails.getIsMdBred());
            if(horseDetails.getFoalingState() != null) horse.setFoalingState(horseDetails.getFoalingState());
            if(horseDetails.getLastCogginDate() != null) horse.setLastCogginDate(horseDetails.getLastCogginDate());
            if(horseDetails.getLastFarrierDate() != null) horse.setLastFarrierDate(horseDetails.getLastFarrierDate());
            if(horseDetails.getMedicalNotes() != null) horse.setMedicalNotes(horseDetails.getMedicalNotes());
            
            // checkHealthAlerts(horse);
            return horseRepository.save(horse);
        }).orElseThrow(() -> new RuntimeException("Horse not found with id " + id));
    }

    public void deleteHorse(Long id) {
        User currentUser = currentUserService.getCurrentUser();
        if(!horseRepository.findByIdAndUserId(id, currentUser.getId()).isPresent()) {
            throw new RuntimeException("Cannot delete. Horse not found with id: " + id);
        }
        horseRepository.deleteById(id);
    }
}
