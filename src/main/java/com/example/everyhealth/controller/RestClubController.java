package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.Club;
import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.ClubCreate;
import com.example.everyhealth.dto.ClubDto;
import com.example.everyhealth.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
@Tag(name = "클럽 관리")
public class RestClubController {

    private final ClubDataService clubDataService;
    private final MemberService memberService;
    private final ClubMemberService clubMemberService;
    private final ClubBusinessService clubBusinessService;

    @PostMapping("/club")
    @Operation(summary = "클럽 저장")
    public ResponseEntity<String> save(@ExtractMemberId Long memberId,
                                     @RequestBody ClubCreate clubCreate,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors.toString());
        }

        Club club = clubBusinessService.createClub(memberId, clubCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(club.getTitle() + "를 생성했습니다.");
    }

    @PostMapping("/club/{id}/join")
    @Operation(summary = "클럽 참가")
    public ResponseEntity<Void> join(@ExtractMemberId Long memberId, @PathVariable Long id) {

        boolean exists = clubMemberService.existsByMemberId(memberId);

        if (!exists) {
            Member member = memberService.findById(memberId);
            Club club = clubDataService.findById(id);

            ClubMember clubMember = new ClubMember(club, member, false, LocalDateTime.now());
            clubMemberService.save(clubMember);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/club/chatRoom/{id}")
    @Operation(summary = "채팅방의 ID로 클럽 조회")
    public ResponseEntity<ClubDto> findByChatRoomId(@PathVariable Long id) {
        ClubDto clubDto = clubDataService.findByChatRoomId(id);
        return ResponseEntity.ok().body(clubDto);
    }

    @GetMapping("/clubs")
    @Operation(summary = "클럽 조건 조회")
    public ResponseEntity<List<ClubDto>> clubList(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) Long isMyClubs,
                                                  @ExtractMemberId Long memberId) {
        if (name == null && isMyClubs == null) {
            return ResponseEntity.ok(clubDataService.fetchAll());
        } else {
            return ResponseEntity.ok(clubDataService.cacheSearchByMemberAndName(isMyClubs, name, memberId));
        }
    }

    @DeleteMapping("/club/{id}/leave")
    @Operation(summary = "클럽 떠나기")
    public ResponseEntity<Void> leaveClub(@PathVariable Long id, @ExtractMemberId Long memberId) {
        ClubMember clubMember = clubMemberService.findByMemberIdAndClubId(memberId, id);

        if (clubMember.isAdmin()){
            clubMemberService.deleteByClubId(id);
            clubDataService.deleteById(id);
        }

        clubMemberService.delete(clubMember);

        return ResponseEntity.ok().build();
    }
}
