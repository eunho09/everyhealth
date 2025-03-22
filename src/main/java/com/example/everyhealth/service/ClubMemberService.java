package com.example.everyhealth.service;

import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubMemberService {

    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public Long save(ClubMember clubMember) {
        clubMemberRepository.save(clubMember);
        return clubMember.getId();
    }

    public ClubMember findById(Long id) {
        return clubMemberRepository.findById(id).get();
    }

    public List<ClubMember> findAll() {
        return clubMemberRepository.findAll();
    }

    public boolean existsByMemberId(Long memberId) {
        return clubMemberRepository.existsByMemberId(memberId);
    }

    @Transactional
    public void delete(ClubMember clubMember) {
        clubMemberRepository.delete(clubMember);
    }

    public ClubMember findByMemberIdAndClubId(Long memberId, Long clubId) {
        return clubMemberRepository.findByMemberIdAndClubId(memberId, clubId);
    }

    public ClubMember findByMemberIdAndChatRoomId(Long memberId, Long chatRoomId) {
        return clubMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId);
    }

    @Transactional
    public void deleteByClubId(Long clubId) {
        clubMemberRepository.deleteByClubId(clubId);
    }
}
