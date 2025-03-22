package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.MemberDto;
import com.example.everyhealth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestMemberController {

    private final MemberService memberService;

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id) {
        Member member = memberService.findById(id);
        return member.getName();
    }

    @GetMapping("/member/friend/{friendId}")
    public MemberDto findByFriendInfo(@PathVariable Long friendId) {
        Member friendInfo = memberService.findByFriendId(friendId);
        return new MemberDto(friendInfo.getId(), friendInfo.getName(), friendInfo.getPicture());
    }
}
