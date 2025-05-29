package com.example.everyhealth.service;

import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.domain.Club;
import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.ClubCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ClubBusinessService {

    private final ClubDataService clubDataService;
    private final MemberService memberService;
    private final ClubMemberService clubMemberService;
    private final ChatRoomService chatRoomService;

    @Transactional
    public Club createClub(Long memberId, ClubCreate clubCreate) {
        Member member = memberService.findById(memberId);
        Club club = new Club(clubCreate.getTitle(), clubCreate.getContent(), clubCreate.getLocation(), clubCreate.getSchedule(), clubCreate.getHighlight());
        clubDataService.save(club);

        ClubMember clubMember = new ClubMember(club, member, true, LocalDateTime.now());
        clubMemberService.save(clubMember);

        ChatRoom chatRoom = new ChatRoom(clubCreate.getTitle(), club);
        chatRoomService.save(chatRoom);

        return club;
    }
}
