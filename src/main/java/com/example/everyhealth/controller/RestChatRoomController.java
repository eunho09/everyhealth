package com.example.everyhealth.controller;

import com.example.everyhealth.service.ChatRoomService;
import com.example.everyhealth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

}
