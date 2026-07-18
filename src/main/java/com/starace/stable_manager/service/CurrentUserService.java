package com.starace.stable_manager.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.model.User;
import com.starace.stable_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
