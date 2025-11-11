package org.devconnect.devconnectbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
public class Chat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;
    
    @Column(name = "project_id", nullable = false)
    private Long projectId;
    
    @Column(name = "last_message")
    private String lastMessage;
    
    @Column(name = "last_message_time")
    private LocalDateTime lastMessageTime;
    
    @Column(name = "unread_count_user1")
    private Integer unreadCountUser1 = 0;
    
    @Column(name = "unread_count_user2")
    private Integer unreadCountUser2 = 0;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Chat() {}
    
    public Chat(User user1, User user2, Long projectId) {
        this.user1 = user1;
        this.user2 = user2;
        this.projectId = projectId;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser1() {
        return user1;
    }
    
    public void setUser1(User user1) {
        this.user1 = user1;
    }
    
    public User getUser2() {
        return user2;
    }
    
    public void setUser2(User user2) {
        this.user2 = user2;
    }
    
    public Long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    public String getLastMessage() {
        return lastMessage;
    }
    
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
    
    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }
    
    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
    
    public Integer getUnreadCountUser1() {
        return unreadCountUser1;
    }
    
    public void setUnreadCountUser1(Integer unreadCountUser1) {
        this.unreadCountUser1 = unreadCountUser1;
    }
    
    public Integer getUnreadCountUser2() {
        return unreadCountUser2;
    }
    
    public void setUnreadCountUser2(Integer unreadCountUser2) {
        this.unreadCountUser2 = unreadCountUser2;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
