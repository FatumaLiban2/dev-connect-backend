package org.devconnect.devconnectbackend.service;

import org.devconnect.devconnectbackend.dto.MessageDTO;
import org.devconnect.devconnectbackend.model.Chat;
import org.devconnect.devconnectbackend.model.Message;
import org.devconnect.devconnectbackend.model.User;
import org.devconnect.devconnectbackend.repository.ChatRepository;
import org.devconnect.devconnectbackend.repository.MessageRepository;
import org.devconnect.devconnectbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    /**
     * Send a message from one user to another
     */
    @Transactional
    public MessageDTO sendMessage(Long senderId, Long receiverId, String content, Long projectId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        
        // Create and save message
        Message message = new Message(sender, receiver, content, projectId);
        message.setStatus(Message.MessageStatus.SENT);
        message = messageRepository.save(message);
        
        // Update or create chat
        Chat chat = chatRepository.findChatBetweenUsersAnyProject(senderId, receiverId)
                .orElseGet(() -> new Chat(sender, receiver, projectId));
        
        chat.setLastMessage(content);
        chat.setLastMessageTime(message.getTimestamp());
        
        // Update unread count for receiver
        if (chat.getUser1().getId().equals(receiverId)) {
            chat.setUnreadCountUser1(chat.getUnreadCountUser1() + 1);
        } else {
            chat.setUnreadCountUser2(chat.getUnreadCountUser2() + 1);
        }
        
        chatRepository.save(chat);
        
        // Convert to DTO and send via WebSocket
        MessageDTO messageDTO = convertToDTO(message);
        messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/messages",
                messageDTO
        );
        
        return messageDTO;
    }
    
    /**
     * Get all messages between two users
     */
    public List<MessageDTO> getMessagesBetweenUsers(Long userId1, Long userId2) {
        List<Message> messages = messageRepository.findMessagesBetweenUsers(userId1, userId2);
        List<MessageDTO> messageDTOs = new ArrayList<>();
        
        for (Message message : messages) {
            messageDTOs.add(convertToDTO(message));
        }
        
        return messageDTOs;
    }
    
    /**
     * Mark messages as read
     */
    @Transactional
    public void markMessagesAsRead(Long senderId, Long receiverId) {
        List<Message> messages = messageRepository.findMessagesBetweenUsers(senderId, receiverId);
        
        for (Message message : messages) {
            if (message.getReceiver().getId().equals(receiverId) && 
                message.getStatus() != Message.MessageStatus.READ) {
                message.setStatus(Message.MessageStatus.READ);
                message.setReadAt(LocalDateTime.now());
                messageRepository.save(message);
                
                // Notify sender about read receipt
                MessageDTO messageDTO = convertToDTO(message);
                messagingTemplate.convertAndSendToUser(
                        senderId.toString(),
                        "/queue/read-receipts",
                        messageDTO
                );
            }
        }
        
        // Update unread count in chat
        Chat chat = chatRepository.findChatBetweenUsersAnyProject(senderId, receiverId)
                .orElse(null);
        
        if (chat != null) {
            if (chat.getUser1().getId().equals(receiverId)) {
                chat.setUnreadCountUser1(0);
            } else {
                chat.setUnreadCountUser2(0);
            }
            chatRepository.save(chat);
        }
    }
    
    /**
     * Mark message as delivered
     */
    @Transactional
    public void markMessageAsDelivered(Long messageId, Long receiverId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        if (message.getReceiver().getId().equals(receiverId)) {
            message.setStatus(Message.MessageStatus.DELIVERED);
            messageRepository.save(message);
            
            // Notify sender about delivery
            MessageDTO messageDTO = convertToDTO(message);
            messagingTemplate.convertAndSendToUser(
                    message.getSender().getId().toString(),
                    "/queue/delivery-receipts",
                    messageDTO
            );
        }
    }
    
    /**
     * Convert Message entity to DTO
     */
    private MessageDTO convertToDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getSender().getId(),
                message.getReceiver().getId(),
                message.getContent(),
                message.getStatus().name().toLowerCase(),
                message.getTimestamp(),
                message.getProjectId()
        );
    }
}
