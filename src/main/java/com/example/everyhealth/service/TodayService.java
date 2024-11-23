package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.domain.Today;
import com.example.everyhealth.domain.TodayExercise;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.repository.ExerciseRepository;
import com.example.everyhealth.repository.RoutineExerciseRepository;
import com.example.everyhealth.repository.TodayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TodayService {

    private final TodayRepository todayRepository;
    private final RoutineExerciseRepository routineExerciseRepository;
    private final ExerciseRepository exerciseRepository;

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

    @Transactional
    public void delete(Today today) {
        todayRepository.delete(today);
    }

    @Transactional
    public void addTodayExerciseAsExercise(TodayExerciseAsExerciseRequest dto) {
        Today today = todayRepository.findById(dto.getTodayId()).get();

        dto.getExerciseInfoList()
                .stream()
                .forEach(exerciseInfo -> {
                    Exercise exercise = exerciseRepository.fetchById(exerciseInfo.getExerciseId());
                    new TodayExercise(exercise, today, exercise.getRepWeight(), exerciseInfo.getSequence());
                });

        todayRepository.save(today);
    }

    @Transactional
    public void addTodayExercise(List<TodayExerciseRequest> todayExerciseRequests, LocalDate date) {
        Today today = todayRepository.fetchByLocalDate(date);

        int baseSequence = today.getTodayExercises().stream()
                .mapToInt(TodayExercise::getSequence)
                .max()
                .orElse(0) + 1;

        for (TodayExerciseRequest request : todayExerciseRequests) {
            if (request.getType().equals("exercise")) {
                Exercise exercise = exerciseRepository.findById(request.getId()).get();
                new TodayExercise(exercise, today, exercise.getRepWeight(), baseSequence++);
            }

            if (request.getType().equals("routine")) {
                List<RoutineExercise> routineExerciseList = routineExerciseRepository.findByRoutineId(request.getId());
                for (RoutineExercise routineExercise : routineExerciseList) {
                    new TodayExercise(routineExercise.getExercise(), today, routineExercise.getRepWeight(), baseSequence++);
                }
            }
        }

        todayRepository.save(today);
    }

    public List<TodayDateDto> findByMonth(int month) {
        return todayRepository.findByMonth(month);
    }


    public TodayDto fetchByLocalDate(LocalDate date) {
        Today today = todayRepository.fetchByLocalDate(date);
        List<TodayExercise> todayExercises = today.getTodayExercises();
        List<TodayExerciseDto> todayExerciseDtoList = todayExercises.stream()
                .map(todayExercise -> new TodayExerciseDto(todayExercise.getExercise().getName(),
                        todayExercise.getRepWeight(),
                        todayExercise.getSequence()))
                .collect(Collectors.toList());

        return new TodayDto(todayExerciseDtoList, today.getLocalDate(), today.getCheckBox());
    }
}
