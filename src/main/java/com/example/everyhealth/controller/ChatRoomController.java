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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @ExtractMemberId
    @PostMapping()
    public ResponseEntity<Void> save(Long memberId, @RequestBody String title) {
        Member member = memberService.findById(memberId);

        ChatRoom chatRoom = new ChatRoom(title, member);
        chatRoomService.save(chatRoom);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
