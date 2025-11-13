package org.devconnect.devconnectbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_conversation_participants", 
                                columnNames = {"participant_a_id", "participant_b_id"})
        },
        indexes = {
                @Index(name = "idx_conversation_last_message", columnList = "last_message_at DESC")
        })
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "participant_a_id", nullable = false)
    private Long participantAId;

    @Column(name = "participant_b_id", nullable = false)
    private Long participantBId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @Column(name = "last_message_preview")
    private String lastMessagePreview;

    @Column(name = "unread_count_a")
    private Integer unreadCountA = 0;

    @Column(name = "unread_count_b")
    private Integer unreadCountB = 0;

    public Conversation() {}

    public Conversation(Long userId1, Long userId2) {
        // Normalize: always store smaller ID as participantA
        if (userId1 < userId2) {
            this.participantAId = userId1;
            this.participantBId = userId2;
        } else {
            this.participantAId = userId2;
            this.participantBId = userId1;
        }
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getParticipantAId() { return participantAId; }
    public void setParticipantAId(Long participantAId) { this.participantAId = participantAId; }

    public Long getParticipantBId() { return participantBId; }
    public void setParticipantBId(Long participantBId) { this.participantBId = participantBId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastMessageAt() { return lastMessageAt; }
    public void setLastMessageAt(LocalDateTime lastMessageAt) { this.lastMessageAt = lastMessageAt; }

    public String getLastMessagePreview() { return lastMessagePreview; }
    public void setLastMessagePreview(String lastMessagePreview) { 
        this.lastMessagePreview = lastMessagePreview != null && lastMessagePreview.length() > 100 
            ? lastMessagePreview.substring(0, 100) 
            : lastMessagePreview;
    }

    public Integer getUnreadCountA() { return unreadCountA; }
    public void setUnreadCountA(Integer unreadCountA) { this.unreadCountA = unreadCountA; }

    public Integer getUnreadCountB() { return unreadCountB; }
    public void setUnreadCountB(Integer unreadCountB) { this.unreadCountB = unreadCountB; }

    /**
     * Get the other participant's ID given one participant's ID
     */
    public Long getOtherParticipantId(Long userId) {
        if (participantAId.equals(userId)) {
            return participantBId;
        } else if (participantBId.equals(userId)) {
            return participantAId;
        }
        throw new IllegalArgumentException("User " + userId + " is not a participant in this conversation");
    }

    /**
     * Get unread count for a specific user
     */
    public Integer getUnreadCountForUser(Long userId) {
        if (participantAId.equals(userId)) {
            return unreadCountA;
        } else if (participantBId.equals(userId)) {
            return unreadCountB;
        }
        return 0;
    }

    /**
     * Increment unread count for a specific user
     */
    public void incrementUnreadForUser(Long userId) {
        if (participantAId.equals(userId)) {
            unreadCountA++;
        } else if (participantBId.equals(userId)) {
            unreadCountB++;
        }
    }

    /**
     * Reset unread count for a specific user
     */
    public void resetUnreadForUser(Long userId) {
        if (participantAId.equals(userId)) {
            unreadCountA = 0;
        } else if (participantBId.equals(userId)) {
            unreadCountB = 0;
        }
    }
}
