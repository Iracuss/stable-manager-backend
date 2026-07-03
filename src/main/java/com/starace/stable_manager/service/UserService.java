package com.starace.stable_manager.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.UserRequest;
import com.starace.stable_manager.enums.Role;
import com.starace.stable_manager.model.User;
import com.starace.stable_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getAllUsers() {
        User currentUser = getCurrentUser();

        if(currentUser.getRole() == Role.ADMIN) {
            return userRepository.findAll();
        }
        
        throw new RuntimeException("Access denied");
    }

    public User getMyAccount() {
        User user = getCurrentUser();
        return user;
    }

    // Going to skip password on purpose right now
    public User updateAccount(UserRequest request) {
        User currentUser = getCurrentUser();

        return userRepository.findById(currentUser.getId()).map(user -> {
            if(request.getEmail() != null) user.setEmail(request.getEmail());
            if(request.getUsername() != null) user.setUsername(request.getUsername());

            user.setId(currentUser.getId());
            user.setPassword(currentUser.getPassword());
            user.setRole(currentUser.getRole());
            user.setHorses(currentUser.getHorses());

            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found."));
    }

    public void deleteUser(Long id) {
        User currentUser = getCurrentUser();

        if(!userRepository.existsById(id)){
            throw new RuntimeException("Cannot delete. User not found with id: " + id);
        } else if(currentUser.getId() != id) {
            throw new RuntimeException("Cannot delete. Current user doesn't match given user");
        }
 
        userRepository.delete(currentUser);
    }
    
}
