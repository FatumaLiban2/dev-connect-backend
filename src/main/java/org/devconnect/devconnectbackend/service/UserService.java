package org.devconnect.devconnectbackend.service;

import org.devconnect.devconnectbackend.dto.UserStatusDTO;
import org.devconnect.devconnectbackend.model.User;
import org.devconnect.devconnectbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    /**
     * Update user online status
     */
    @Transactional
    public void updateUserStatus(Long userId, User.UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setStatus(status);
        if (status == User.UserStatus.OFFLINE) {
            user.setLastSeen(LocalDateTime.now());
        }
        userRepository.save(user);
        
        // Broadcast status change to all users
        UserStatusDTO statusDTO = new UserStatusDTO(userId, status.name().toLowerCase());
        messagingTemplate.convertAndSend("/topic/user-status", statusDTO);
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    /**
     * Get user status
     */
    public String getUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getStatus().name().toLowerCase();
    }
}
