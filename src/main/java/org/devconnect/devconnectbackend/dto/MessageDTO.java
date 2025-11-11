package org.devconnect.devconnectbackend.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String text;
    private String status;
    private LocalDateTime timestamp;
    private Long projectId;
    
    // Constructors
    public MessageDTO() {}
    
    public MessageDTO(Long id, Long senderId, Long receiverId, String text, 
                      String status, LocalDateTime timestamp, Long projectId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.status = status;
        this.timestamp = timestamp;
        this.projectId = projectId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public Long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
