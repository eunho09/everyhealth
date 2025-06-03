package com.example.everyhealth.controller;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.MemberDto;
import com.example.everyhealth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "회원 관리")
public class RestMemberController {

    private final MemberService memberService;

    @GetMapping("/member/{id}")
    @Operation(summary = "회원 조회")
    public String findById(@PathVariable Long id) {
        Member member = memberService.findById(id);
        return member.getName();
    }

    @GetMapping("/member/friend/{friendId}")
    @Operation(summary = "친구 ID로 나를 찾기")
    public MemberDto findByFriendInfo(@PathVariable Long friendId) {
        return memberService.findByFriendId(friendId);
    }
}
