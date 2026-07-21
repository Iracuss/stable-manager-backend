package com.starace.stable_manager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starace.stable_manager.dto.HorseRequest;
import com.starace.stable_manager.service.HorseService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getAllHorsesInStable(@PathVariable Long stableId) {
        return ResponseEntity.ok(horseService.getAllHorsesInStable(stableId));
    }

    @GetMapping("/{stableId}/{id}")
    public ResponseEntity<?> getHorseById(@PathVariable Long id, @PathVariable Long stableId) {
        return ResponseEntity.ok(horseService.getHorseById(id, stableId));
    }

    @PostMapping("/{stableId}")
    public ResponseEntity<?> createHorse(@PathVariable Long stableId, @RequestBody HorseRequest horse) {        
        return ResponseEntity.ok(horseService.saveHorse(stableId, horse));
    }
    
    @PutMapping("/{stableId}/{id}")
    public ResponseEntity<?> updateHorse(@PathVariable Long id, @PathVariable Long stableId, @RequestBody HorseRequest horseDetails) {
        return ResponseEntity.ok(horseService.updateHorse(id, stableId, horseDetails));
    }

    @DeleteMapping("/{stableId}/{id}")
    public ResponseEntity<?> deleteHorse(@PathVariable Long id, @PathVariable Long stableId) {
        horseService.deleteHorse(id, stableId);
        return ResponseEntity.ok("Horse deleted successfully");
    }
    
}
