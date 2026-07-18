package com.starace.stable_manager.repository;

import com.starace.stable_manager.model.Horse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorseRepository extends JpaRepository<Horse, Long>{
    List<Horse> findByStableId(Long stableId);
    Optional<Horse> findByIdAndStableId(Long id, Long stableId);
}
