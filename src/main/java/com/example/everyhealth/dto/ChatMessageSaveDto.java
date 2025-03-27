package com.example.everyhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatMessageSaveDto {

    private String message;
    private Long messageId;
    private MemberChatResponseDto member;
    private LocalDateTime createdDate;

}
