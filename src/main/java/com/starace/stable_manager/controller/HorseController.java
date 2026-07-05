package com.starace.stable_manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.starace.stable_manager.dto.HorseRequest;
import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.service.HorseService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/horses")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class HorseController {

    private final HorseService horseService;

    @GetMapping
    public List<Horse> getAllHorses() {
        return horseService.getAllHorses();
    }

    @GetMapping("/{id}")
    public Horse getHorseById(@PathVariable Long id) {
        return horseService.getHorseById(id);
    }

    @PostMapping
    public Horse createHorse(@RequestBody HorseRequest horse) {        
        return horseService.saveHorse(horse);
    }
    
    @PutMapping("/{id}")
    public Horse updateHorse(@PathVariable Long id, @RequestBody HorseRequest horseDetails) {
        return horseService.updateHorse(id, horseDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteHorse(@PathVariable Long id) {
        horseService.deleteHorse(id);
    }
    
}
