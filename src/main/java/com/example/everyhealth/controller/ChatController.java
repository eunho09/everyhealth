package com.example.everyhealth.controller;


import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.ChatMessage;
import com.example.everyhealth.dto.ChatMessageRequest;
import com.example.everyhealth.dto.ChatMessageResponseDto;
import com.example.everyhealth.dto.MemberChatResponseDto;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.ChatMessageService;
import com.example.everyhealth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @GetMapping("/api/rooms/{roomId}/recentMessage")
    public ResponseEntity<List<ChatMessageResponseDto>> recentMessage(@RequestParam(defaultValue = "10") int limit, @PathVariable Long roomId) {
        List<ChatMessage> byRecentMessage = chatMessageService.findByRecentMessage(roomId, limit);
        List<ChatMessageResponseDto> chatMessageList = byRecentMessage.stream()
                .map(m -> new ChatMessageResponseDto(
                        m.getMessage(),
                        m.getId(),
                        new MemberChatResponseDto(m.getMember().getId(), m.getMember().getName(), m.getMember().getPicture()),
                        m.getCreatedDate()))
                .toList();

        return ResponseEntity.ok().body(chatMessageList);
    }

    @GetMapping("/api/rooms/{roomId}/olderMessage")
    public ResponseEntity<List<ChatMessageResponseDto>> olderMessage(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) Long messageId) {
        List<ChatMessage> olderMessages = chatMessageService.findOlderMessages(roomId, messageId, limit);
        List<ChatMessageResponseDto> list = olderMessages.stream()
                .map(m -> new ChatMessageResponseDto(
                        m.getMessage(),
                        m.getId(),
                        new MemberChatResponseDto(m.getMember().getId(), m.getMember().getName(), m.getMember().getPicture()),
                        m.getCreatedDate()))
                .toList();

        return ResponseEntity.ok().body(list);
    }
}
