package com.example.everyhealth.dto;

import com.example.everyhealth.domain.ChatMessage;
import com.example.everyhealth.domain.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageResponseDto {
    private String message;
    private Long messageId;
    private LocalDateTime createdDate;
    private MemberChatResponseDto member;

    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.message = chatMessage.getMessage();
        this.messageId = chatMessage.getId();
        this.member = new MemberChatResponseDto(chatMessage.getClubMember());
        this.createdDate = chatMessage.getCreatedDate();
    }
}
