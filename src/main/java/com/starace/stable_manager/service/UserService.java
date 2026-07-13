package com.starace.stable_manager.service;

import com.starace.stable_manager.security.JwtService;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.starace.stable_manager.dto.UserRequest;
import com.starace.stable_manager.dto.UserResponse;
import com.starace.stable_manager.enums.Role;
import com.starace.stable_manager.model.User;
import com.starace.stable_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
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

    public UserResponse getMyAccount() {
        User currentUser = getCurrentUser();
        UserResponse response = new UserResponse();
        response.setId(currentUser.getId());
        response.setEmail(currentUser.getEmail());
        response.setUsername(currentUser.getUsername());
        response.setRole(currentUser.getRole());
        return response;
    }

    // Going to skip password on purpose right now
    public String updateAccount(UserRequest request) {
        User currentUser = getCurrentUser();

        if(request.getEmail() != null) currentUser.setEmail(request.getEmail());
        if(request.getUsername() != null) currentUser.setUsername(request.getUsername());

        User savedUser = userRepository.save(currentUser);

        // UserResponse response = new UserResponse();
        // response.setEmail(savedUser.getEmail());
        // response.setUsername(savedUser.getUsername());
        // response.setRole(savedUser.getRole());
        return jwtService.generateToken(savedUser);
    }

    public void deleteUser(Long id) {
        User currentUser = getCurrentUser();

        if(!userRepository.existsById(id)){
            throw new RuntimeException("Cannot delete. User not found with id: " + id);
        } else if(!currentUser.getId().equals(id)) {
            throw new RuntimeException("Cannot delete. Current user doesn't match given user");
        }
 
        userRepository.delete(currentUser);
    }
    
}
