package com.example.everyhealth.controller;


import com.example.everyhealth.domain.ChatMessage;
import com.example.everyhealth.dto.ChatMessageResponseDto;
import com.example.everyhealth.dto.ChatMessageSaveDto;
import com.example.everyhealth.dto.MemberChatResponseDto;
import com.example.everyhealth.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "채팅 관리")
public class RestChatController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/rooms/{roomId}/send")
    @SendTo("/topic/public/rooms/{roomId}")
    @Operation(summary = "채팅 보내기")
    public ResponseEntity<ChatMessageSaveDto> sendMessage(@DestinationVariable Long roomId, @Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        Long memberId = (Long) headerAccessor.getSessionAttributes().get("memberId");

        ChatMessageSaveDto chatMessageSaveDto = chatMessageService.saveMessage(message, roomId, memberId);

        return ResponseEntity.ok().body(chatMessageSaveDto);
    }

    @GetMapping("/api/rooms/{roomId}/recentMessage")
    @Operation(summary = "최근순 채팅 내역")
    public ResponseEntity<List<ChatMessageResponseDto>> recentMessage(@RequestParam(defaultValue = "10") int limit, @PathVariable Long roomId) {
        List<ChatMessage> byRecentMessage = chatMessageService.findByRecentMessage(roomId, limit);
        List<ChatMessageResponseDto> chatMessageList = byRecentMessage.stream()
                .map(m -> new ChatMessageResponseDto(m))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(chatMessageList);
    }

    @GetMapping("/api/rooms/{roomId}/olderMessage")
    @Operation(summary = "오래된순 채팅 내역")
    public ResponseEntity<List<ChatMessageResponseDto>> olderMessage(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) Long messageId) {
        List<ChatMessage> olderMessages = chatMessageService.findOlderMessages(roomId, messageId, limit);
        List<ChatMessageResponseDto> list = olderMessages.stream()
                .map(m -> new ChatMessageResponseDto(m))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(list);
    }
}
