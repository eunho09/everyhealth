package com.example.everyhealth.service;

import com.example.everyhealth.aop.ClearTodayCache;
import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.repository.ExerciseRepository;
import com.example.everyhealth.repository.RoutineExerciseRepository;
import com.example.everyhealth.repository.TodayExerciseRepository;
import com.example.everyhealth.repository.TodayRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TodayDataService {

    private final TodayRepository todayRepository;
    private final TodayExerciseRepository todayExerciseRepository;

    @Transactional
    @ClearTodayCache
    public Long save(Today today) {
        todayRepository.save(today);
        return today.getId();
    }

    public Today findById(Long id) {
        return todayRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 오늘이 존재하지 않습니다. ID : " + id));
    }

    public List<Today> findAll() {
        return todayRepository.findAll();
    }

    public List<Today> fetchMemberId(Long memberId) {
        return todayRepository.fetchByMemberId(memberId);
    }

    @Transactional
    @ClearTodayCache
    public void delete(Today today) {
        todayRepository.delete(today);
    }

    public Today fetchWithTodayExercises(LocalDate date, Long memberId){
        return todayRepository.fetchWithTodayExercises(date, memberId);
    }

    @Cacheable(value = "todayByYearAndMonth", key = "{#year, #month, #memberId}")
    public List<TodayDateDto> findByYearAndMonth(int year, int month, Long memberId) {
        return todayRepository.findByYearAndMonth(year, month, memberId);
    }


    @Cacheable(value = "todayByLocalDate", key = "{#date, #memberId}")
    public TodayDto fetchByLocalDate(LocalDate date, Long memberId) {
        Today today = todayRepository.findByLocalDateAndMemberId(date, memberId);
        List<TodayExercise> todayExerciseList = todayExerciseRepository.fetchByTodayId(today.getId());

        return new TodayDto(today, todayExerciseList);
    }

    public Today fetchByIdWithTodayExercises(Long todayId) {
        return todayRepository.fetchByIdWithTodayExercises(todayId);
    }


    public Boolean existsByMemberIdAndDate(Long memberId, LocalDate date) {
        return todayRepository.existsByMemberIdAndDate(memberId, date);
    }
}
