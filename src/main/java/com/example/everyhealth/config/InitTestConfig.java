package com.example.everyhealth.config;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitTestConfig {

    private final MemberService memberService;

    @PostConstruct
    public void init() {
        log.info("기본 테스트 데이터 실행");

        Member member = new Member("홍길동", "test", "test");
        memberService.save(member);
    }
}
