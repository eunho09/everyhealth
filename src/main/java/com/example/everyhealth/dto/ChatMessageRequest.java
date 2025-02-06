package com.example.everyhealth.dto;

import com.example.everyhealth.domain.Member;
import lombok.Data;

@Data
public class ChatMessageRequest {

    private String message;
    private Member member;
}
