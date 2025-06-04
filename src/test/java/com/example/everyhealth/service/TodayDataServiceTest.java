package com.example.everyhealth.service;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.MemberRole;
import com.example.everyhealth.exception.TodayException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "file:C:/coding/spring/project/everyhealth/src/main/resources/.env-local")
class TodayDataServiceTest {

    @Autowired
    TodayDataService todayDataService;

    @Autowired
    TodayBusinessService todayBusinessService;

    @Autowired
    MemberService memberService;

    private Long memberId;
    private Long member2Id;

    @BeforeEach
    void init() {
        // 멤버 생성 및 저장
        Member member = new Member("길동", "test1", MemberRole.USER, "test1", "test1");
        memberId = memberService.save(member);

        Member member2 = new Member("홍길", "test2", MemberRole.USER, "test2", "test2");
        member2Id = memberService.save(member2);
    }

    @Test
    @DisplayName("동일 날짜 Today 엔티티 생성 에러")
    void DuplicateTodayDate() {
        Long todayId1 = todayBusinessService.createToday(memberId, LocalDate.of(2025, 5, 30));
        Assertions.assertThrows(TodayException.class, () -> {
            todayBusinessService.createToday(memberId, LocalDate.of(2025, 5, 30));
        });
    }
}