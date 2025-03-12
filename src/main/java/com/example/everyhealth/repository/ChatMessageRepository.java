package com.example.everyhealth.repository;

import com.example.everyhealth.domain.ChatMessage;
import com.example.everyhealth.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE m.chatRoom.id = :roomId " +
            "ORDER BY m.createdDate desc")
    Page<ChatMessage> findByRecentMessage(@Param("roomId") Long roomId, Pageable pageable);


    @Query("select m from ChatMessage m where m.id <:messageId and m.chatRoom.id =:roomId order by m.createdDate desc")
    Page<ChatMessage> findOlderMessages(@Param("roomId") Long roomId, @Param("messageId") Long messageId, PageRequest pageRequest);
}
