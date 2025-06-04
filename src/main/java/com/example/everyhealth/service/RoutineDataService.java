package com.example.everyhealth.service;

import com.example.everyhealth.aop.ClearRoutineCache;
import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.domain.Routine;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.repository.ExerciseRepository;
import com.example.everyhealth.repository.RoutineExerciseRepository;
import com.example.everyhealth.repository.RoutineRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoutineDataService {

    private final RoutineRepository routineRepository;

    @Transactional
    @CachePut(value = "routines" , key = "#routine.id")
    @CacheEvict(value = {"allRoutines", "routinesByMember"}, allEntries = true)
    public Long save(Routine routine) {
        routineRepository.save(routine);
        return routine.getId();
    }

    public Routine findById(Long id) {
        return routineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 루틴이 존재하지 않습니다. ID : " + id));
    }

    public List<Routine> findAll() {
        return routineRepository.findAll();
    }

    public List<Routine> fetchByMemberId(Long memberId) {
        return routineRepository.fetchByMemberId(memberId);
    }

    @Transactional
    @CacheEvict(value = "routines", key = "#routine.id")
    public void delete(Routine routine) {
        routineRepository.delete(routine);
    }

    @Transactional
    @ClearRoutineCache
    public void deleteById(Long routineId) {
        routineRepository.deleteById(routineId);
    }

}
