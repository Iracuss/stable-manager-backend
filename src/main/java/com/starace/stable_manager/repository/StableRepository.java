package com.starace.stable_manager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.starace.stable_manager.model.Stable;

@Repository
public interface StableRepository extends JpaRepository<Stable, Long> {
    Optional<Stable> findById(Long id);
}
