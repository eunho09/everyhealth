package com.example.everyhealth.security;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(id)).orElseThrow(() -> new UsernameNotFoundException("해당 하는 유저가 없습니다."));
        return new CustomUserDetails(member);
    }
}
