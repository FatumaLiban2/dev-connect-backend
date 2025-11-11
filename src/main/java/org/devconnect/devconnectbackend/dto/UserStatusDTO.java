package org.devconnect.devconnectbackend.dto;

public class UserStatusDTO {
    private Long userId;
    private String status;
    
    // Constructors
    public UserStatusDTO() {}
    
    public UserStatusDTO(Long userId, String status) {
        this.userId = userId;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
