package com.starace.stable_manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starace.stable_manager.dto.HorseRequest;
import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.service.HorseService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/horses")
@AllArgsConstructor
public class HorseController {

    private final HorseService horseService;

    @GetMapping("/{stableId}")
    public List<Horse> getAllHorsesInStable(@PathVariable Long stableId) {
        return horseService.getAllHorsesInStable(stableId);
    }

    @GetMapping("/{id}/{stableId}")
    public Horse getHorseById(@PathVariable Long id, @PathVariable Long stableId) {
        return horseService.getHorseById(id, stableId);
    }

    @PostMapping("/{stableId}")
    public Horse createHorse(@PathVariable Long stableId, @RequestBody HorseRequest horse) {        
        return horseService.saveHorse(stableId, horse);
    }
    
    @PutMapping("/{id}/{stableId}")
    public Horse updateHorse(@PathVariable Long id, @PathVariable Long stableId, @RequestBody HorseRequest horseDetails) {
        return horseService.updateHorse(id, stableId, horseDetails);
    }

    @DeleteMapping("/{id}/{stableId}")
    public void deleteHorse(@PathVariable Long id, @PathVariable Long stableId) {
        horseService.deleteHorse(id, stableId);
    }
    
}
