package com.example.everyhealth.service;

import com.example.everyhealth.aop.ClearTodayCache;
import com.example.everyhealth.domain.*;
import com.example.everyhealth.dto.*;
import com.example.everyhealth.exception.ErrorCode;
import com.example.everyhealth.exception.TodayException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayBusinessService {

    private final MemberService memberService;
    private final TodayDataService todayDataService;
    private final TodayExerciseDataService todayExerciseDataService;
    private final ExerciseDataService exerciseDataService;
    private final RoutineExerciseService routineExerciseService;

    @Transactional
    public Long createToday(Long memberId, LocalDate date) {
        Member member = memberService.findById(memberId);
        if (todayDataService.existsByMemberIdAndDate(member.getId(), date)){
            throw new TodayException(ErrorCode.TODAY_DUPLICATE_BY_DATE, date);
        }

        Today today = new Today(date, member);
        return todayDataService.save(today);
    }

    public List<TodayDto> memberTodays(Long memberId) {
        List<Today> todays = todayDataService.fetchMemberId(memberId);
        List<TodayExercise> todayExercises = todayExerciseDataService.fetchByTodayIdIn(todays.stream().map(t -> t.getId()).toList());

        Map<Long, TodayExercise> todayExerciseMap = todayExercises.stream()
                .collect(Collectors.toMap(te -> te.getId(), dto -> dto));

        return todays.stream()
                .map(t -> new TodayDto(t, todayExerciseMap))
                .collect(Collectors.toList());
    }

    @Transactional
    @ClearTodayCache
    public void updateCheckbox(boolean checked, Long todayId) {
        Today today = todayDataService.findById(todayId);
        if (checked){
            today.setCheckBox(CheckBox.True);
        } else{
            today.setCheckBox(CheckBox.False);
        }
    }

    @Cacheable(value = "todays", key = "#todayId")
    public TodayDto fetchById(Long todayId){
        Today today = todayDataService.findById(todayId);
        List<TodayExercise> todayExerciseList = todayExerciseDataService.fetchByTodayId(todayId);

        return new TodayDto(today, todayExerciseList);
    }

    @Transactional
    @ClearTodayCache
    public void addTodayExercise(List<TodayExerciseRequest> todayExerciseRequests, LocalDate date, Long memberId) {
        Today today = todayDataService.fetchWithTodayExercises(date, memberId);

        int baseSequence = today.getTodayExercises().stream()
                .mapToInt(TodayExercise::getSequence)
                .max()
                .orElse(0) + 1;

        for (TodayExerciseRequest request : todayExerciseRequests) {
            if (request.getType().equals("exercise")) {
                Exercise exercise = exerciseDataService.fetchById(request.getId());
                TodayExercise todayExercise = new TodayExercise(exercise, today, baseSequence++);

                List<RepWeight> repWeightList = exercise.getRepWeightList();

                for (RepWeight repWeight : repWeightList) {
                    new RepWeight(repWeight.getReps(), repWeight.getWeight(), todayExercise);
                }
            }

            if (request.getType().equals("routine")) {
                List<RoutineExercise> routineExerciseList = routineExerciseService.findByRoutineId(request.getId());
                for (RoutineExercise routineExercise : routineExerciseList) {
                    TodayExercise todayExercise = new TodayExercise(routineExercise.getExercise(), today, baseSequence++);

                    List<RepWeight> repWeightList = routineExercise.getRepWeightList();
                    for (RepWeight repWeight : repWeightList) {
                        new RepWeight(repWeight.getReps(), repWeight.getWeight(), todayExercise);
                    }
                }
            }
        }

        todayDataService.save(today);
    }

    @Transactional
    @ClearTodayCache
    public void updateTodayExercise(List<UpdateTodayExerciseDto> updateDtoList, Long todayId) {
        List<TodayExercise> todayExerciseList = todayExerciseDataService.fetchByTodayId(todayId);

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
    @ClearTodayCache
    public void updateSequence(List<UpdateSeqTodayExercise> updateSeqTodayExerciseList, Long todayId) {
        Today today = todayDataService.fetchByIdWithTodayExercises(todayId);

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


}
