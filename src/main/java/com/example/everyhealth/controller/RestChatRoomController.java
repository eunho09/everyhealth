package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.service.ChatRoomService;
import com.example.everyhealth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MemberService memberService;


    @GetMapping("/member/rooms")
    public ResponseEntity<List<ChatRoom>> memberRooms(@ExtractMemberId Long memberId) {
        List<ChatRoom> chatRooms = chatRoomService.fetchByMemberId(memberId);
        return ResponseEntity.ok(chatRooms);
    }
}
