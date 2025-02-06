package com.example.everyhealth.controller;


import com.example.everyhealth.dto.ChatMessageRequest;
import com.example.everyhealth.dto.ChatMessageResponseDto;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/rooms/{roomId}/send")
    @SendTo("/topic/public/rooms/{roomId}")
    public ResponseEntity<ChatMessageResponseDto> sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageRequest chatMessage, @CookieValue(name = "jwt") String token) {
        Long memberId = jwtTokenGenerator.getUserId(token);
        ChatMessageResponseDto chatMessageResponseDto = chatMessageService.chatMessageResponse(chatMessage.getMessage(), roomId, memberId);

        return ResponseEntity.ok().body(chatMessageResponseDto);
    }
}
