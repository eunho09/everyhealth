package com.example.everyhealth.service;

import com.example.everyhealth.domain.Club;
import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.MemberRole;
import com.example.everyhealth.dto.ClubCreate;
import com.example.everyhealth.dto.ClubDto;
import com.example.everyhealth.repository.ClubMemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "file:C:/coding/spring/project/everyhealth/src/main/resources/.env-local")
@Slf4j
class ClubBusinessServiceTest {

    @Autowired
    ClubBusinessService clubBusinessService;
    @Autowired
    ClubDataService clubDataService;
    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;

    public Long memberId;
    public Long memberId2;

    @BeforeEach
    void init() {
        Member member = new Member("길동", "test1", MemberRole.USER, "test1", "test1");
        memberId = memberService.save(member);

        Member member2 = new Member("홍길", "test2", MemberRole.USER, "test2", "test2");
        memberId2 = memberService.save(member2);
    }

    @Test
    @DisplayName("클럽 생성")
    void create() {
        ClubCreate clubCreate = new ClubCreate("배드민턴 클럽", "많이 와주세요", "서울", "매주 화요일", "자유롭게 활동하세요");
        Club club = clubBusinessService.createClub(memberId, clubCreate);


        assertThat(club.getClubMemberList())
                .hasSize(1)
                .extracting(cm -> cm.getMember().getId())
                .contains(memberId);

        assertThat(club.getChatRoom())
                .extracting(cr -> cr.getClub().getTitle())
                .isEqualTo(club.getTitle());
    }

    @Test
    @DisplayName("클럽 참가 예외발생")
    void join() {
        ClubCreate clubCreate = new ClubCreate("배드민턴 클럽", "많이 와주세요", "서울", "매주 화요일", "자유롭게 활동하세요");
        Club club = clubBusinessService.createClub(memberId, clubCreate);

        clubBusinessService.join(club.getId(), memberId2);

        Assertions.assertThrows(AccessDeniedException.class, () -> clubBusinessService.join(club.getId(), memberId2));
        Assertions.assertThrows(AccessDeniedException.class, () -> clubBusinessService.join(club.getId(), memberId));
    }

    @Test
    @DisplayName("클럽 떠나기")
    void leave() {
        ClubCreate clubCreate = new ClubCreate("배드민턴 클럽", "많이 와주세요", "서울", "매주 화요일", "자유롭게 활동하세요");
        Club club = clubBusinessService.createClub(memberId, clubCreate);
        clubBusinessService.join(club.getId(), memberId2);

        clubBusinessService.leaveClub(club.getId(), memberId2);

        em.clear();

        Club findClub = clubDataService.fetchById(club.getId());

        assertThat(findClub.getClubMemberList())
                .hasSize(1)
                .extracting(cm -> cm.getMember().getId())
                .contains(memberId);
    }

    @Test
    @DisplayName("방장이 클럽 떠나기")
    void leaveAdmin() {
        ClubCreate clubCreate = new ClubCreate("배드민턴 클럽", "많이 와주세요", "서울", "매주 화요일", "자유롭게 활동하세요");
        Club club = clubBusinessService.createClub(memberId, clubCreate);
        clubBusinessService.leaveClub(club.getId(), memberId);

        em.clear();

        Assertions.assertThrows(EntityNotFoundException.class, () -> clubDataService.findById(club.getId()));
    }

    @Test
    @DisplayName("입력 후 클럽리스트")
    void getSearch() {
        ClubCreate clubCreate = new ClubCreate("배드민턴 화요일 클럽", "많이 와주세요", "서울", "매주 화요일", "자유롭게 활동하세요");
        Club club = clubBusinessService.createClub(memberId, clubCreate);

        ClubCreate clubCreate2 = new ClubCreate("배드민턴 수요일 클럽", "많이 와주세요", "서울", "매주 수요일", "자유롭게 활동하세요");
        Club club2 = clubBusinessService.createClub(memberId, clubCreate2);

        em.clear();
        List<ClubDto> clubOrSearch = clubBusinessService.getClubOrSearch("배드민턴", 0L, memberId);

        assertThat(clubOrSearch)
                .hasSize(2)
                .extracting(c -> c.getTitle())
                .contains("배드민턴 화요일 클럽", "배드민턴 수요일 클럽");
    }

    @Test
    @DisplayName("나의 클럽리스트")
    void getMyClub() {
        ClubCreate clubCreate = new ClubCreate("배드민턴 화요일 클럽", "많이 와주세요", "서울", "매주 화요일", "자유롭게 활동하세요");
        Club club = clubBusinessService.createClub(memberId, clubCreate);

        ClubCreate clubCreate2 = new ClubCreate("배드민턴 수요일 클럽", "많이 와주세요", "서울", "매주 수요일", "자유롭게 활동하세요");
        Club club2 = clubBusinessService.createClub(memberId2, clubCreate2);

        em.clear();
        List<ClubDto> clubOrSearch = clubBusinessService.getClubOrSearch("배드민턴", 1L, memberId);

        assertThat(clubOrSearch)
                .hasSize(1)
                .extracting(c -> c.getTitle())
                .contains("배드민턴 화요일 클럽");
    }

    @Test
    @DisplayName("모든 클럽 조회")
    void getClubs() {
        ClubCreate clubCreate = new ClubCreate("배드민턴 화요일 클럽", "많이 와주세요", "서울", "매주 화요일", "자유롭게 활동하세요");
        Club club = clubBusinessService.createClub(memberId, clubCreate);

        ClubCreate clubCreate2 = new ClubCreate("배드민턴 수요일 클럽", "많이 와주세요", "서울", "매주 수요일", "자유롭게 활동하세요");
        Club club2 = clubBusinessService.createClub(memberId2, clubCreate2);

        em.clear();
        List<ClubDto> clubOrSearch = clubBusinessService.getClubOrSearch("", 0L, memberId);

        assertThat(clubOrSearch)
                .hasSize(2)
                .extracting(c -> c.getTitle())
                .contains("배드민턴 화요일 클럽", "배드민턴 수요일 클럽");
    }

}