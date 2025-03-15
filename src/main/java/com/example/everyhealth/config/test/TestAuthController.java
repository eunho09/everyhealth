package com.example.everyhealth.config.test;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.repository.MemberRepository;
import com.example.everyhealth.security.JwtTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Profile("test") // 테스트 환경에서만 활성화
@RequestMapping("/api/test")
@Slf4j
public class TestAuthController {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final MemberRepository memberRepository;

    public TestAuthController(JwtTokenGenerator jwtTokenGenerator, MemberRepository memberRepository) {
        log.info("testAuthController");
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> getTestToken() {
        Member testMember = memberRepository.findByName("test")
                .orElseThrow(() -> new RuntimeException("테스트 사용자가 없습니다"));

        // 토큰 생성
        JwtTokenGenerator.TokenInfo tokenInfo = jwtTokenGenerator.generateTokenSet(testMember);

        Map<String, String> response = new HashMap<>();
        response.put("access_token", tokenInfo.getAccessToken());

        return ResponseEntity.ok(response);
    }
}