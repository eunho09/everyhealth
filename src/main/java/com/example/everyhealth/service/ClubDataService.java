package com.example.everyhealth.service;

import com.example.everyhealth.domain.Club;
import com.example.everyhealth.dto.ClubDto;
import com.example.everyhealth.repository.ClubRepository;
import com.example.everyhealth.repository.ClubSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubDataService {

    private final ClubRepository clubRepository;

    @Transactional
    @CachePut(value = "clubs", key = "#club.id")
    @CacheEvict(value = {"clubsByChatRoom", "clubsAll"}, allEntries = true)
    public Long save(Club club) {
        Club saveClub = clubRepository.save(club);
        return saveClub.getId();
    }

    public Club findById(Long id) {
        return clubRepository.findById(id).get();
    }

    @Cacheable(value = "clubsAll")
    public List<ClubDto> fetchAll() {
        List<Club> clubList = clubRepository.fetchAll();

        return clubList.stream()
                .map(c -> new ClubDto(c))
                .collect(Collectors.toList());
    }

    public List<Club> findAll(Specification<Club> spec) {
        return clubRepository.findAll(spec);
    }

    @Transactional
    @CacheEvict(value = {"clubs", "clubsByChatRoom", "clubsAll"}, allEntries = true)
    public void delete(Club club) {
        clubRepository.delete(club);
    }

    @Cacheable(value = "clubsByChatRoom", key = "#chatRoomId")
    public ClubDto findByChatRoomId(Long chatRoomId) {
        Club club = clubRepository.findByChatRoomId(chatRoomId);
        return new ClubDto(club);
    }

    public List<ClubDto> searchClubByMemberAndName(Long memberId, String name) {
        List<Club> clubList = findAll(
                Specification
                        .where(ClubSpecification.joinMember(memberId))
                        .and(ClubSpecification.nameContains(name))

        );

        return clubList.stream()
                .map(c -> new ClubDto(c))
                .collect(Collectors.toList());
    }

    public List<ClubDto> cacheSearchByMemberAndName(Long isMyClubs, String name, Long memberId) {
        Long memberIdToUse = (isMyClubs == null || isMyClubs == 0) ? isMyClubs : memberId;
        return searchClubByMemberAndName(memberIdToUse, name);
    }

    @Transactional
    @CacheEvict(value = {"clubs", "clubsByChatRoom"}, allEntries = true)
    public void deleteById(Long id) {
        clubRepository.deleteById(id);
    }
}
