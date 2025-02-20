package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Member;
import lombok.Data;

@Data
public class ChatMessageResponseDto {
    private String message;
    private MemberChatResponseDto member;

    public ChatMessageResponseDto(String message, MemberChatResponseDto member) {
        this.message = message;
        this.member = member;
    }
}
