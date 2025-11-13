package org.devconnect.devconnectbackend.repository;

import org.devconnect.devconnectbackend.model.Conversation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * Find conversation between two users (normalized pair)
     */
    @Query("SELECT c FROM Conversation c WHERE " +
           "(c.participantAId = :smallerId AND c.participantBId = :largerId)")
    Optional<Conversation> findByParticipantPair(@Param("smallerId") Long smallerId, 
                                                  @Param("largerId") Long largerId);

    /**
     * Find all conversations for a user, ordered by most recent message
     */
    @Query("SELECT c FROM Conversation c WHERE " +
           "c.participantAId = :userId OR c.participantBId = :userId " +
           "ORDER BY c.lastMessageAt DESC NULLS LAST, c.createdAt DESC")
    List<Conversation> findByUserIdOrderByLastMessageAtDesc(@Param("userId") Long userId, Pageable pageable);

    /**
     * Find all conversations for a user (unpaged)
     */
    @Query("SELECT c FROM Conversation c WHERE " +
           "c.participantAId = :userId OR c.participantBId = :userId " +
           "ORDER BY c.lastMessageAt DESC NULLS LAST, c.createdAt DESC")
    List<Conversation> findByUserId(@Param("userId") Long userId);
}
