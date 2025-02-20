package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.ChatRoomService;
import com.example.everyhealth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MemberService memberService;


/*
    @PostMapping("/chatRoom")
    public ResponseEntity<Void> save(@ExtractMemberId Long memberId, @RequestBody String title) {
        Member member = memberService.findById(memberId);

        ChatRoom chatRoom = new ChatRoom(title, member);
        chatRoomService.save(chatRoom);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
*/

    @PostMapping("/chatRoom/{roomId}/join")
    public ResponseEntity<Void> join(@ExtractMemberId Long memberId, @PathVariable Long roomId) {
        Member member = memberService.findById(memberId);
        ChatRoom chatRoom = chatRoomService.findById(roomId);

        return ResponseEntity.ok().build();
    }
}
