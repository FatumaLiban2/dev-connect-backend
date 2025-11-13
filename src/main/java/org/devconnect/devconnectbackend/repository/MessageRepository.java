package org.devconnect.devconnectbackend.repository;

import org.devconnect.devconnectbackend.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    /**
     * Find messages by conversation ID, ordered by timestamp ascending
     */
    @Query("SELECT m FROM Message m WHERE m.conversationId = :conversationId " +
           "ORDER BY m.timestamp ASC")
    List<Message> findByConversationIdOrderByTimestampAsc(@Param("conversationId") Long conversationId);
    
    /**
     * Find messages by conversation ID with pagination, ordered by timestamp descending (latest first)
     */
    @Query("SELECT m FROM Message m WHERE m.conversationId = :conversationId " +
           "ORDER BY m.timestamp DESC")
    List<Message> findByConversationIdOrderByTimestampDesc(@Param("conversationId") Long conversationId, Pageable pageable);
    
    /**
     * Count unread messages in a conversation for a specific receiver
     */
    @Query("SELECT COUNT(m) FROM Message m WHERE " +
           "m.conversationId = :conversationId AND m.senderId = :senderId AND m.status != 'READ'")
    Integer countUnreadMessages(@Param("conversationId") Long conversationId, 
                                @Param("senderId") Long senderId);
    
    /**
     * Find unread messages in a conversation sent by a specific sender
     */
    @Query("SELECT m FROM Message m WHERE " +
           "m.conversationId = :conversationId AND m.senderId = :senderId AND m.status != 'READ' " +
           "ORDER BY m.timestamp ASC")
    List<Message> findUnreadMessagesBySender(@Param("conversationId") Long conversationId, 
                                             @Param("senderId") Long senderId);
}

