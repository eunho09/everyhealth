package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.domain.Club;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.ClubCreate;
import com.example.everyhealth.dto.ClubDto;
import com.example.everyhealth.service.ChatRoomService;
import com.example.everyhealth.service.ClubService;
import com.example.everyhealth.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ClubController {

    private final ClubService clubService;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @PostMapping("/club")
    public ResponseEntity<Void> save(@ExtractMemberId Long memberId, @RequestBody ClubCreate clubCreate) {
        Member member = memberService.findById(memberId);
        Club club = new Club(clubCreate.getTitle(), clubCreate.getContent(), member, clubCreate.getLocation(), clubCreate.getSchedule(), clubCreate.getHighlights());
        clubService.save(club);

        ChatRoom chatRoom = new ChatRoom(clubCreate.getTitle(), member, club);
        chatRoomService.save(chatRoom);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/clubs")
    public ResponseEntity<List<ClubDto>> clubList() {
        List<Club> clubList = clubService.findAll();
        List<ClubDto> clubDtoList = clubList.stream()
                .map(club -> new ClubDto(club.getTitle(), club.getContent(), club.getLocation(), club.getSchedule(), club.getHighlights(), club.getChatRoom().getId()))
                .toList();
        return ResponseEntity.ok(clubDtoList);
    }
}
