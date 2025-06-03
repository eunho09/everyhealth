package com.example.everyhealth.service;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.MemberDto;
import com.example.everyhealth.exception.ErrorCode;
import com.example.everyhealth.exception.MemberException;
import com.example.everyhealth.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
        return memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다. ID : " + id));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public List<MemberDto> findSuggestedFriend(Long memberId) {
        return memberRepository.findSuggestedFriend(memberId);
    }

    @Cacheable(value = "memberByFriend", key = "#friendId")
    public MemberDto findByFriendId(Long friendId) {
        Member friendInfo = memberRepository.findByFriendId(friendId);
        return new MemberDto(friendInfo);
    }

    @Cacheable(value = "existsByIdAndFriendId", key = "{#id, #friendId}")
    public void existsByIdAndFriendId(Long id, Long friendId) {
        boolean exists = memberRepository.existsByIdAndFriendId(id, friendId);

        if (!exists){
            throw new MemberException(ErrorCode.MEMBER_NOT_FRIEND, id, friendId);
        }
    }
}
