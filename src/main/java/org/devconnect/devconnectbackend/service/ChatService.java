package org.devconnect.devconnectbackend.service;

import org.devconnect.devconnectbackend.dto.ChatDTO;
import org.devconnect.devconnectbackend.model.Chat;
import org.devconnect.devconnectbackend.model.User;
import org.devconnect.devconnectbackend.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    
    @Autowired
    private ChatRepository chatRepository;
    
    /**
     * Get all chats for a user
     * Returns only chats with users of opposite role (client sees developers, developer sees clients)
     */
    public List<ChatDTO> getChatsByUserId(Long userId) {
        List<Chat> chats = chatRepository.findChatsByUserId(userId);
        List<ChatDTO> chatDTOs = new ArrayList<>();
        
        for (Chat chat : chats) {
            // Determine the other user in the chat
            User otherUser = chat.getUser1().getId().equals(userId) 
                    ? chat.getUser2() 
                    : chat.getUser1();
            
            // Determine unread count for current user
            Integer unreadCount = chat.getUser1().getId().equals(userId)
                    ? chat.getUnreadCountUser1()
                    : chat.getUnreadCountUser2();
            
            ChatDTO chatDTO = new ChatDTO(
                    chat.getId(),
                    otherUser.getId(),
                    otherUser.getUsername(),
                    otherUser.getAvatar(),
                    otherUser.getRole().name().toLowerCase(),
                    otherUser.getStatus().name().toLowerCase(),
                    chat.getLastMessage(),
                    chat.getLastMessageTime(),
                    unreadCount,
                    chat.getProjectId()
            );
            
            chatDTOs.add(chatDTO);
        }
        
        return chatDTOs;
    }
    
    /**
     * Get or create a chat between two users
     */
    public Chat getOrCreateChat(Long userId1, Long userId2, Long projectId) {
        return chatRepository.findChatBetweenUsers(userId1, userId2, projectId)
                .orElseGet(() -> {
                    // Chat doesn't exist, would need User entities to create
                    // This is typically handled in MessageService when first message is sent
                    throw new RuntimeException("Chat not found. Send a message to create a chat.");
                });
    }
}
