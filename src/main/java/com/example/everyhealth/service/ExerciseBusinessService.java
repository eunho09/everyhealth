package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.ExerciseUpdateDto;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.dto.ExerciseCreateDto;
import com.example.everyhealth.dto.ExerciseResponseDto;
import com.example.everyhealth.dto.RepWeightDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExerciseBusinessService {

    private final MemberService memberService;
    private final ExerciseDataService exerciseDataService;
    private final RepWeightService repWeightService;


    @Transactional
    public Long createExercise(Long memberId, ExerciseCreateDto dto) {
        Member findMember = memberService.findById(memberId);

        Exercise exercise = new Exercise(dto.getName(),
                findMember,
                dto.getMemo(),
                dto.getClassification());

        List<RepWeightDto> repWeightList = dto.getRepWeightList();
        repWeightList.forEach(r -> new RepWeight(r.getReps(), r.getWeight(), exercise));

        exerciseDataService.save(exercise);

        return exercise.getId();
    }

    @Transactional
    @CachePut(value = "exercises", key = "#id")
    @CacheEvict(value = {"exercisesByMember", "exerciseAll"}, allEntries = true)
    public void update(Long id, ExerciseUpdateDto dto) {
        Exercise exercise = exerciseDataService.fetchById(id);
        if (dto.getName() != null) {
            exercise.setName(dto.getName());
        }
        if (dto.getMemo() != null) {
            exercise.setMemo(dto.getMemo());
        }
        if (dto.getClassification() != null) {
            exercise.setClassification(dto.getClassification());
        }
        if (dto.getRepWeightList() != null) {
            List<RepWeightDto> newRepWeights = dto.getRepWeightList();
            List<RepWeight> existingRepWeights = exercise.getRepWeightList();

            Map<Long, RepWeight> existingRepWeightMap = existingRepWeights.stream()
                    .filter(rw -> rw.getId() != null)
                    .collect(Collectors.toMap(RepWeight::getId, rw -> rw));

            List<RepWeight> updatedRepWeights = new ArrayList<>();

            for (RepWeightDto newRepWeight : newRepWeights) {
                if (newRepWeight.getId() == null) {

                    RepWeight repWeight = new RepWeight(newRepWeight.getReps(), newRepWeight.getWeight(), exercise);
                    updatedRepWeights.add(repWeight);
                } else if (existingRepWeightMap.containsKey(newRepWeight.getId())) {

                    RepWeight existingRepWeight = existingRepWeightMap.get(newRepWeight.getId());
                    existingRepWeight.setReps(newRepWeight.getReps());
                    existingRepWeight.setWeight(newRepWeight.getWeight());
                    updatedRepWeights.add(existingRepWeight);

                    existingRepWeightMap.remove(newRepWeight.getId());
                }
            }


            existingRepWeights.clear();
            existingRepWeights.addAll(updatedRepWeights);
        }
    }

    @Transactional
    public void delete(Long id) {
        repWeightService.deleteByExerciseId(id);
        exerciseDataService.deleteById(id);
    }

    @Cacheable(value = "exercisesByMember", key = "#memberId")
    public List<ExerciseResponseDto> fetchMemberExercises(Long memberId) {
        List<Exercise> exercises = exerciseDataService.findExercisesByMemberId(memberId);

        return exercises.stream()
                .map(ExerciseResponseDto::new)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "exercises", key = "#id")
    public ExerciseResponseDto fetchOne(Long id){
        Exercise findExercise = exerciseDataService.fetchById(id);
        return new ExerciseResponseDto(findExercise);
    }

    @Cacheable(value = "exerciseAll")
    public List<ExerciseResponseDto> fetchAll() {
        List<Exercise> exercises = exerciseDataService.fetchAll();
        return exercises.stream()
                .map(ExerciseResponseDto::new)
                .collect(Collectors.toList());

    }
}
