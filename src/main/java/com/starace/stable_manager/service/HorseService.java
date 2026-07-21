package com.starace.stable_manager.service;

import java.util.ArrayList;
// import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.HorseRequest;
import com.starace.stable_manager.dto.HorseResponse;
import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.model.Stable;
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
    private final MembershipService membershipService;

    public HorseResponse saveHorse(Long stableId, HorseRequest request) {
        Optional<Stable> optStable = stableRepository.findById(stableId);

        if(optStable.isEmpty()) {
            throw new RuntimeException("No stable exists with id: " + stableId);
        }

        // Check if member of stable
        if(!membershipService.checkEditMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a member of this stable");
        }

        Stable stable = optStable.get();

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
        horse.setStable(stable);

        HorseResponse response = toHorseResponse(horse);
        horseRepository.save(horse);

        return response;
    }

    public List<HorseResponse> getAllHorsesInStable(Long stableId) {
        // Check if user is member of stable
        if(!membershipService.checkMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a member of this stable");
        }

        List<Horse> horses = horseRepository.findByStableId(stableId);
        List<HorseResponse> responses = new ArrayList<>();

        horses.forEach(horse -> {
            HorseResponse response = toHorseResponse(horse);
            responses.add(response);
        });

        return responses;
    }

    public HorseResponse getHorseById(Long id, Long stableId) {
        Horse horse = horseRepository.findByIdAndStableId(id, stableId)
            .orElseThrow(() -> new EntityNotFoundException("Horse not found with id: " + id));

        return toHorseResponse(horse);
    }

    public HorseResponse updateHorse(Long id, Long stableId, HorseRequest horseDetails) {
        // Check if member of stable
        if(!membershipService.checkEditMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a member of this stable");
        }

        return horseRepository.findByIdAndStableId(id, stableId).map(horse -> {
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
            
            HorseResponse response = toHorseResponse(horse);
            horseRepository.save(horse);

            return response;
        }).orElseThrow(() -> new RuntimeException("Horse not found with id " + id));
    }

    public void deleteHorse(Long id, Long stableId) {
        // Check if member of stable
        if(!membershipService.checkEditMembershipStatus(stableId)) {
            throw new RuntimeException("User is not a member of this stable");
        }

        if(!horseRepository.findByIdAndStableId(id, stableId).isPresent()) {
            throw new RuntimeException("Cannot delete. Horse not found with id: " + id);
        }
        horseRepository.deleteById(id);
    }

    public HorseResponse toHorseResponse(Horse horse) {
        HorseResponse response = new HorseResponse();
        response.setId(horse.getId());
        response.setName(horse.getName());
        response.setNickname(horse.getNickname());
        response.setBreed(horse.getBreed());
        response.setBirthYear(horse.getBirthYear());
        response.setMicrochipId(horse.getMicrochipId());
        response.setIsMdBred(horse.getIsMdBred());
        response.setFoalingState(horse.getFoalingState());
        response.setLastCogginDate(horse.getLastCogginDate());
        response.setLastFarrierDate(horse.getLastFarrierDate());
        response.setMedicalNotes(horse.getMedicalNotes());
        response.setStableId(horse.getStable().getId());

        return response;
    }
}
