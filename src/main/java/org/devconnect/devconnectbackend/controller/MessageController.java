package org.devconnect.devconnectbackend.controller;

import org.devconnect.devconnectbackend.dto.ChatDTO;
import org.devconnect.devconnectbackend.dto.MessageDTO;
import org.devconnect.devconnectbackend.model.User;
import org.devconnect.devconnectbackend.service.ChatService;
import org.devconnect.devconnectbackend.service.MessageService;
import org.devconnect.devconnectbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private ChatService chatService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Get all chats for a user
     * GET /api/messages/chats/{userId}
     */
    @GetMapping("/chats/{userId}")
    public ResponseEntity<List<ChatDTO>> getUserChats(@PathVariable Long userId) {
        try {
            List<ChatDTO> chats = chatService.getChatsByUserId(userId);
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get messages between two users
     * GET /api/messages/conversation?userId1={id1}&userId2={id2}
     */
    @GetMapping("/conversation")
    public ResponseEntity<List<MessageDTO>> getConversation(
            @RequestParam Long userId1,
            @RequestParam Long userId2) {
        try {
            List<MessageDTO> messages = messageService.getMessagesBetweenUsers(userId1, userId2);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Send a message (REST endpoint alternative to WebSocket)
     * POST /api/messages/send
     */
    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        try {
            MessageDTO sentMessage = messageService.sendMessage(
                    messageDTO.getSenderId(),
                    messageDTO.getReceiverId(),
                    messageDTO.getText(),
                    messageDTO.getProjectId()
            );
            return ResponseEntity.ok(sentMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Mark messages as read
     * PUT /api/messages/read
     */
    @PutMapping("/read")
    public ResponseEntity<Map<String, String>> markAsRead(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        try {
            messageService.markMessagesAsRead(senderId, receiverId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Messages marked as read");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update user online status
     * PUT /api/messages/status/{userId}
     */
    @PutMapping("/status/{userId}")
    public ResponseEntity<Map<String, String>> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam String status) {
        try {
            User.UserStatus userStatus = User.UserStatus.valueOf(status.toUpperCase());
            userService.updateUserStatus(userId, userStatus);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Status updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get user status
     * GET /api/messages/status/{userId}
     */
    @GetMapping("/status/{userId}")
    public ResponseEntity<Map<String, String>> getUserStatus(@PathVariable Long userId) {
        try {
            String status = userService.getUserStatus(userId);
            Map<String, String> response = new HashMap<>();
            response.put("status", status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
