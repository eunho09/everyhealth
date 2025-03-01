package com.example.everyhealth.service;

import com.example.everyhealth.domain.Club;
import com.example.everyhealth.dto.ClubDto;
import com.example.everyhealth.repository.ClubRepository;
import com.example.everyhealth.repository.ClubSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubRepository clubRepository;

    @Transactional
    public Long save(Club club) {
        Club saveClub = clubRepository.save(club);
        return saveClub.getId();
    }

    public Club findById(Long id) {
        return clubRepository.findById(id).get();
    }

    public List<Club> findAll() {
        return clubRepository.findAll();
    }

    public List<Club> findAll(Specification<Club> spec) {
        return clubRepository.findAll(spec);
    }

    @Transactional
    public void delete(Club club) {
        clubRepository.delete(club);
    }

    public ClubDto findByChatRoomId(Long chatRoomId) {
        Club club = clubRepository.findByChatRoomId(chatRoomId);
        return new ClubDto(club.getId(), club.getTitle(), club.getContent(), club.getLocation(), club.getSchedule(), club.getHighlights(), club.getChatRoom().getId());
    }

    public List<ClubDto> searchClubByMemberAndName(Long memberId, String name) {
        List<Club> clubList = findAll(
                Specification
                        .where(ClubSpecification.joinMember(memberId))
                        .and(ClubSpecification.nameContains(name))

        );

        return clubList.stream()
                .map(c -> new ClubDto(
                        c.getId(),
                        c.getTitle(),
                        c.getContent(),
                        c.getLocation(),
                        c.getSchedule(),
                        c.getHighlights(),
                        c.getChatRoom().getId()))
                .toList();
    }
}
