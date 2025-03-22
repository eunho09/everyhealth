package com.example.everyhealth.service;

import com.example.everyhealth.domain.TodayExercise;
import com.example.everyhealth.repository.TodayExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayExerciseService {

    private final TodayExerciseRepository todayExerciseRepository;

    @Transactional
    public Long save(TodayExercise todayExercise) {
        todayExerciseRepository.save(todayExercise);
        return todayExercise.getId();
    }


    public TodayExercise findById(Long id) {
        return todayExerciseRepository.findById(id).get();
    }

    public List<TodayExercise> findAll() {
        return todayExerciseRepository.findAll();
    }

    @Transactional
    public void delete(TodayExercise todayExercise){
        todayExerciseRepository.delete(todayExercise);
    }

    public List<TodayExercise> fetchByTodayId(Long todayId) {
        return todayExerciseRepository.fetchByTodayId(todayId);
    }

    public List<TodayExercise> fetchByTodayIdIn(List<Long> todayIds) {
        return todayExerciseRepository.fetchByTodayIdIn(todayIds);
    }

    @Transactional
    public void deleteById(Long id) {
        todayExerciseRepository.deleteById(id);
    }
}
