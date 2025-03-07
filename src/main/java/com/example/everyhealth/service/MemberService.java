package com.example.everyhealth.service;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.MemberDto;
import com.example.everyhealth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).get();
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public List<MemberDto> findSuggestedFriend(Long memberId) {
        return memberRepository.findSuggestedFriend(memberId);
    }

    public Member findByFriendInfo(Long friendId) {
        return memberRepository.findByFriendInfo(friendId);
    }
}
