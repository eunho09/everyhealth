package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageResponseDto {
    private String message;
    private Long messageId;
    private LocalDateTime createdDate;
    private MemberChatResponseDto member;

    public ChatMessageResponseDto(String message, Long messageId, MemberChatResponseDto member, LocalDateTime createdDate) {
        this.message = message;
        this.messageId = messageId;
        this.member = member;
        this.createdDate = createdDate;
    }
}
