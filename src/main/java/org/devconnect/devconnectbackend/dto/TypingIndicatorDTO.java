package org.devconnect.devconnectbackend.dto;

public class TypingIndicatorDTO {
    private Long senderId;
    private Long receiverId;
    private boolean isTyping;
    
    // Constructors
    public TypingIndicatorDTO() {}
    
    public TypingIndicatorDTO(Long senderId, Long receiverId, boolean isTyping) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.isTyping = isTyping;
    }
    
    // Getters and Setters
    public Long getSenderId() {
        return senderId;
    }
    
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    
    public Long getReceiverId() {
        return receiverId;
    }
    
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
    
    public boolean isTyping() {
        return isTyping;
    }
    
    public void setTyping(boolean typing) {
        isTyping = typing;
    }
}
