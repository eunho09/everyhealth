package com.example.everyhealth.service;

import com.example.everyhealth.domain.Today;
import com.example.everyhealth.domain.TodayExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TodayExerciseBusinessService {

    private final TodayExerciseDataService todayExerciseDataService;
    private final RepWeightService repWeightService;
    private final TodayService todayService;

    @Transactional
    public String deleteTodayExercise(Long id) {
        TodayExercise todayExercise = todayExerciseDataService.findById(id);

        if (todayExercise == null) {
            return "todayExercise가 존재하지 않습니다.";
        }

        Today today = todayExercise.getToday();
        repWeightService.deleteByTodayExerciseId(id);
        todayExerciseDataService.deleteById(id);

        if (today != null) {
            List<TodayExercise> todayExerciseList = todayExerciseDataService.findByTodayId(today.getId());
            if (todayExerciseList.isEmpty()) {
                todayService.delete(today);
                return "delete today and todayExercise";
            }
        }
        return "delete todayExercise";
    }


}

