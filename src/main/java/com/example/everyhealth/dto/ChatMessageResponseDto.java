package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Member;
import lombok.Data;

@Data
public class ChatMessageResponseDto {
    private String message;
    private Member member;

    public ChatMessageResponseDto(String message, Member member) {
        this.message = message;
        this.member = member;
    }
}
