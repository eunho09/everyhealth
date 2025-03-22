package com.example.everyhealth.service;

import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.repository.ExerciseRepository;
import com.example.everyhealth.repository.RoutineExerciseRepository;
import com.example.everyhealth.repository.TodayExerciseRepository;
import com.example.everyhealth.repository.TodayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TodayService {

    private final TodayRepository todayRepository;
    private final TodayExerciseRepository todayExerciseRepository;
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

    public List<Today> fetchMemberId(Long memberId) {
        return todayRepository.fetchByMemberId(memberId);
    }

    @Transactional
    public void delete(Today today) {
        todayRepository.delete(today);
    }

    @Transactional
    public void addTodayExercise(List<TodayExerciseRequest> todayExerciseRequests, LocalDate date, Long memberId) {
        Today today = todayRepository.fetchWithTodayExercises(date, memberId);

        int baseSequence = today.getTodayExercises().stream()
                .mapToInt(TodayExercise::getSequence)
                .max()
                .orElse(0) + 1;

        for (TodayExerciseRequest request : todayExerciseRequests) {
            if (request.getType().equals("exercise")) {
                Exercise exercise = exerciseRepository.fetchById(request.getId());
                TodayExercise todayExercise = new TodayExercise(exercise, today, baseSequence++);

                List<RepWeight> repWeightList = exercise.getRepWeightList();

                for (RepWeight repWeight : repWeightList) {
                    new RepWeight(repWeight.getReps(), repWeight.getWeight(), todayExercise);
                }
            }

            if (request.getType().equals("routine")) {
                List<RoutineExercise> routineExerciseList = routineExerciseRepository.findByRoutineId(request.getId());
                for (RoutineExercise routineExercise : routineExerciseList) {
                    TodayExercise todayExercise = new TodayExercise(routineExercise.getExercise(), today, baseSequence++);

                    List<RepWeight> repWeightList = routineExercise.getRepWeightList();
                    for (RepWeight repWeight : repWeightList) {
                        new RepWeight(repWeight.getReps(), repWeight.getWeight(), todayExercise);
                    }
                }
            }
        }

        todayRepository.save(today);
    }

    public List<TodayDateDto> findByYearAndMonth(int year, int month, Long memberId) {
        return todayRepository.findByYearAndMonth(year, month, memberId);
    }


    public TodayDto fetchByLocalDate(LocalDate date, Long memberId) {
        Today today = todayRepository.findByLocalDateAndMemberId(date, memberId);
        List<TodayExercise> todayExerciseList = todayExerciseRepository.fetchByTodayId(today.getId());

        List<TodayExerciseDto> todayExerciseDtoList = todayExerciseList.stream()
                .map(todayExercise -> new TodayExerciseDto(
                        todayExercise.getId(),
                        todayExercise.getExercise().getName(),
                        todayExercise.getRepWeightList().stream()
                                .map(rw -> new RepWeightDto(rw.getId(), rw.getReps(), rw.getWeight()))
                                .toList(),
                        todayExercise.getSequence()
                ))
                .collect(Collectors.toList());

        return new TodayDto(today.getId(), todayExerciseDtoList, today.getLocalDate(), today.getCheckBox());
    }

    @Transactional
    public void updateTodayExercise(List<UpdateTodayExerciseDto> updateDtoList, Long todayId) {
        List<TodayExercise> todayExerciseList = todayExerciseRepository.fetchByTodayId(todayId);

        Map<Long, UpdateTodayExerciseDto> updateDtoMap = updateDtoList.stream()
                .collect(Collectors.toMap(UpdateTodayExerciseDto::getId, dto -> dto));

        for (TodayExercise todayExercise : todayExerciseList) {
            Long todayExerciseId = todayExercise.getId();

            if (updateDtoMap.containsKey(todayExerciseId)) {
                UpdateTodayExerciseDto updateTodayExerciseDto = updateDtoMap.get(todayExerciseId);
                List<RepWeight> newRepWeightList = updateTodayExerciseDto.getRepWeightList();
                List<RepWeight> existsRepWeights = todayExercise.getRepWeightList();

                Set<Long> newRepWeightIds = newRepWeightList.stream()
                        .map(RepWeight::getId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                existsRepWeights.removeIf(rw -> !newRepWeightIds.contains(rw.getId()));

                for (RepWeight newRepWeight : newRepWeightList) {
                    if (newRepWeight.getId() == null) {
                        new RepWeight(newRepWeight.getReps(), newRepWeight.getWeight(), todayExercise);
                    } else {
                        existsRepWeights.stream()
                                .filter(rw -> rw.getId().equals(newRepWeight.getId()))
                                .findFirst()
                                .ifPresent(rw -> {
                                    rw.setReps(newRepWeight.getReps());
                                    rw.setWeight(newRepWeight.getWeight());
                                });
                    }
                }
            }
        }
    }

    @Transactional
    public void updateSequence(List<UpdateSeqTodayExercise> updateSeqTodayExerciseList, Long todayId) {
        Today today = todayRepository.fetchByIdWithTodayExercises(todayId);

        List<TodayExercise> todayExercises = today.getTodayExercises();

        for (UpdateSeqTodayExercise request : updateSeqTodayExerciseList) {
            for (TodayExercise todayExercise : todayExercises) {
                if (request.getId().equals(todayExercise.getId())) {
                    todayExercise.setSequence(request.getSequence());
                }
            }
        }

        todayExercises.sort(Comparator.comparingInt(te -> te.getSequence()));
        for (int i = 0; i < todayExercises.size(); i++) {
            todayExercises.get(i).setSequence(i + 1);
        }
    }

    @Transactional
    public void updateCheckbox(boolean checked, Long todayId) {
        Today today = todayRepository.findById(todayId).get();
        if (checked){
            today.setCheckBox(CheckBox.True);
        } else{
            today.setCheckBox(CheckBox.False);
        }
    }
}
