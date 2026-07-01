package com.starace.stable_manager.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.starace.stable_manager.model.Horse;
import com.starace.stable_manager.repository.HorseRepository;
@Configuration
public class DatabaseSeeder {
    
    @Bean
    CommandLineRunner initDatabase(HorseRepository horseRepository) {
        return args -> {
            if(horseRepository.count() == 0) {
                // Missing some attributes
                Horse horse = new Horse();
                horse.setName("Oguri Cap");
                horse.setNickname("Grey Ghost");
                horse.setBreed("Thoroughbred");
                horse.setBirthYear(1985);
                horse.setMicrochipId("MC-985001");
                horse.setIsMdBred(false);
                horse.setFoalingState("Hokkaido");
                horse.setLastCogginDate(LocalDate.now().minusMonths(6));
                horse.setLastFarrierDate(LocalDate.now().minusWeeks(1));
                horse.setMedicalNotes("Check front left shoe regularly.");
                Horse savedHorse = horseRepository.save(horse);

                System.out.println("Successfully seeded database with test data.");
                System.out.println("Horse id: " + savedHorse.getId());
            }
        };
    }
}
