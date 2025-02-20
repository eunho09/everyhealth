package com.example.everyhealth.controller;


import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.dto.ChatMessageRequest;
import com.example.everyhealth.dto.ChatMessageResponseDto;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.ChatMessageService;
import com.example.everyhealth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/rooms/{roomId}/send")
    @SendTo("/topic/public/rooms/{roomId}")
    public ResponseEntity<ChatMessageResponseDto> sendMessage(@DestinationVariable Long roomId, @Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        Long memberId = (Long) headerAccessor.getSessionAttributes().get("memberId");

        ChatMessageResponseDto chatMessageResponseDto = chatMessageService.saveMessage(message, roomId, memberId);

        return ResponseEntity.ok().body(chatMessageResponseDto);
    }
}
