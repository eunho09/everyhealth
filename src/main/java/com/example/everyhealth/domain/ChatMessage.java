package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class ChatMessage {

    @Id
    @GeneratedValue
    private Long id;

    private String message;

    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_member_id")
    private ClubMember clubMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public ChatMessage(String message, ClubMember clubMember, ChatRoom chatRoom) {
        this.message = message;
        this.clubMember = clubMember;
        clubMember.getChatMessageList().add(this);
        this.chatRoom = chatRoom;
        chatRoom.getChatMessageList().add(this);
        this.createdDate = LocalDateTime.now();
    }

    public ChatMessage() {
    }
}

