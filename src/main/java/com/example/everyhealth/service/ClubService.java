package com.example.everyhealth.service;

import com.example.everyhealth.domain.Club;
import com.example.everyhealth.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
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
}
