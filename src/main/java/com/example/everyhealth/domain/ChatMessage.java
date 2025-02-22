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
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public ChatMessage(String message, Member member, ChatRoom chatRoom) {
        this.message = message;
        this.member = member;
        member.getChatMessageList().add(this);
        this.chatRoom = chatRoom;
        chatRoom.getChatMessageList().add(this);
        this.createdDate = LocalDateTime.now();
    }

    public ChatMessage() {
    }
}

