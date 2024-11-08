package com.example.everyhealth.service;

import com.example.everyhealth.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @Transactional
    void save() {
        Member member = new Member("홍길동", "test", "test");

        Long memberId = memberService.save(member);
        Member findMember = memberService.findById(memberId);

        Assertions.assertThat(findMember.getName()).isEqualTo("홍길동");
        Assertions.assertThat(findMember.getLoginId()).isEqualTo("test");
    }

}