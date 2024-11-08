package com.example.everyhealth.service;

import com.example.everyhealth.domain.Today;
import com.example.everyhealth.repository.TodayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayService {

    private final TodayRepository todayRepository;

    @Transactional
    public Long save(Today today) {
        todayRepository.save(today);
        return today.getId();
    }

    public Today findById(Long id) {
        return todayRepository.findById(id).get();
    }

    public List<Today> findAll() {
        return todayRepository.findAll();
    }
}
