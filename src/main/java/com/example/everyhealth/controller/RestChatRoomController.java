package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.service.ChatRoomService;
import com.example.everyhealth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "채팅창 관리")
public class RestChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MemberService memberService;


    @GetMapping("/member/rooms")
    @Operation(summary = "나의 채팅방")
    public ResponseEntity<List<ChatRoom>> memberRooms(@ExtractMemberId Long memberId) {
        List<ChatRoom> chatRooms = chatRoomService.fetchByMemberId(memberId);
        return ResponseEntity.ok(chatRooms);
    }
}
