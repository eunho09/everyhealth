package com.example.everyhealth.service;

import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.repository.ClubMemberRepository;
import jakarta.persistence.EntityNotFoundException;
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
        return clubMemberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("클럽회원이 존재하지 않습니다. ID : " + id));
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

    @Transactional
    public void deleteByClubId(Long clubId) {
        clubMemberRepository.deleteByClubId(clubId);
    }
}
