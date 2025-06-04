package com.example.everyhealth.service;

import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.domain.Club;
import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.ClubCreate;
import com.example.everyhealth.dto.ClubDto;
import com.example.everyhealth.repository.ClubSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public void join(Long cludId, Long memberId) {
        boolean exists = clubMemberService.existsByMemberId(memberId);

        if (!exists) {
            throw new AccessDeniedException("이미 가입된 클럽입니다.");
        }

        Member member = memberService.findById(memberId);
        Club club = clubDataService.findById(cludId);

        ClubMember clubMember = new ClubMember(club, member, false, LocalDateTime.now());
        clubMemberService.save(clubMember);
    }


    public List<ClubDto> getClubOrSearch(String name, Long isMyClubs, Long memberId) {
        if (name == null && isMyClubs == null) {
            return fetchAll();
        } else {
            return cacheSearchByMemberAndName(isMyClubs, name, memberId);
        }
    }

    @Transactional
    public void leaveClub(Long id, Long memberId) {
        ClubMember clubMember = clubMemberService.findByMemberIdAndClubId(memberId, id);

        if (clubMember.isAdmin()){
            clubMemberService.deleteByClubId(id);
            clubDataService.deleteById(id);
        }

        clubMemberService.delete(clubMember);
    }


    @Cacheable(value = "clubsAll")
    public List<ClubDto> fetchAll() {
        List<Club> clubList = clubDataService.fetchAll();

        return clubList.stream()
                .map(c -> new ClubDto(c))
                .collect(Collectors.toList());
    }

    public List<ClubDto> searchClubByMemberAndName(Long memberId, String name) {
        List<Club> clubList = clubDataService.findAll(
                Specification
                        .where(ClubSpecification.joinMember(memberId))
                        .and(ClubSpecification.nameContains(name))

        );

        return clubList.stream()
                .map(c -> new ClubDto(c))
                .collect(Collectors.toList());
    }

    public List<ClubDto> cacheSearchByMemberAndName(Long isMyClubs, String name, Long memberId) {
        Long memberIdToUse = (isMyClubs == null || isMyClubs == 0) ? isMyClubs : memberId;
        return searchClubByMemberAndName(memberIdToUse, name);
    }
}
