package com.example.everyhealth.config.test;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.repository.MemberRepository;
import com.example.everyhealth.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Profile("test")
public class TestAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final MemberRepository memberRepository;

    public TestAuthenticationFilter(CustomUserDetailsService userDetailsService, MemberRepository memberRepository) {
        this.userDetailsService = userDetailsService;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/test/")) {
            Member member = memberRepository.findByName("test")
                    .orElseThrow(() -> new RuntimeException("테스트 회원이 없습니다."));

            UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(member.getId()));

            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}
