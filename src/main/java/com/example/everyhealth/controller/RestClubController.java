package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.domain.Club;
import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.ClubCreate;
import com.example.everyhealth.dto.ClubDto;
import com.example.everyhealth.service.ChatRoomService;
import com.example.everyhealth.service.ClubMemberService;
import com.example.everyhealth.service.ClubService;
import com.example.everyhealth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class RestClubController {

    private final ClubService clubService;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final ClubMemberService clubMemberService;

    @PostMapping("/club")
    public ResponseEntity<Void> save(@ExtractMemberId Long memberId, @RequestBody ClubCreate clubCreate) {
        Member member = memberService.findById(memberId);
        Club club = new Club(clubCreate.getTitle(), clubCreate.getContent(), clubCreate.getLocation(), clubCreate.getSchedule(), clubCreate.getHighlight());
        clubService.save(club);

        ClubMember clubMember = new ClubMember(club, member, true, LocalDateTime.now());
        clubMemberService.save(clubMember);

        ChatRoom chatRoom = new ChatRoom(clubCreate.getTitle(), club);
        chatRoomService.save(chatRoom);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/club/{id}/join")
    public ResponseEntity<Void> join(@ExtractMemberId Long memberId, @PathVariable Long id) {

        boolean exists = clubMemberService.existsByMemberId(memberId);

        if (!exists) {
            Member member = memberService.findById(memberId);
            Club club = clubService.findById(id);

            ClubMember clubMember = new ClubMember(club, member, false, LocalDateTime.now());
            clubMemberService.save(clubMember);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/club/chatRoom/{id}")
    public ResponseEntity<ClubDto> findByChatRoomId(@PathVariable Long id) {
        ClubDto clubDto = clubService.findByChatRoomId(id);
        return ResponseEntity.ok().body(clubDto);
    }

    @GetMapping("/clubs")
    public ResponseEntity<List<ClubDto>> clubList(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) Long isMyClubs,
                                                  @ExtractMemberId Long memberId) {
        if (name == null && isMyClubs == null) {
            return ResponseEntity.ok(clubService.fetchAll());
        } else {
            return ResponseEntity.ok(clubService.cacheSearchByMemberAndName(isMyClubs, name, memberId));
        }
    }

    @DeleteMapping("/club/{id}/leave")
    public ResponseEntity<Void> leaveClub(@PathVariable Long id, @ExtractMemberId Long memberId) {
        ClubMember clubMember = clubMemberService.findByMemberIdAndClubId(memberId, id);

        if (clubMember.isAdmin()){
            clubMemberService.deleteByClubId(id);
            clubService.deleteById(id);
        }

        clubMemberService.delete(clubMember);

        return ResponseEntity.ok().build();
    }
}
