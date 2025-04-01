package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.ExerciseUpdateDto;
import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.dto.ExerciseDto;
import com.example.everyhealth.dto.ExerciseResponseDto;
import com.example.everyhealth.repository.ExerciseRepository;
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
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Transactional
    @CachePut(value = "exercises", key = "#exercise.id")
    @CacheEvict(value = {"exercisesByMember", "exerciseAll"}, allEntries = true)
    public Long save(Exercise exercise) {
        exerciseRepository.save(exercise);
        return exercise.getId();
    }

    public Exercise findById(Long id) {
        return exerciseRepository.findById(id).get();
    }

    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Transactional
    @CachePut(value = "exercises", key = "#id")
    @CacheEvict(value = {"exercisesByMember", "exerciseAll"}, allEntries = true)
    public void update(Long id, ExerciseUpdateDto dto) {
        Exercise exercise = fetchById(id);
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
            List<RepWeight> newRepWeights = dto.getRepWeightList();
            List<RepWeight> existingRepWeights = exercise.getRepWeightList();

            Map<Long, RepWeight> existingRepWeightMap = existingRepWeights.stream()
                    .filter(rw -> rw.getId() != null)
                    .collect(Collectors.toMap(RepWeight::getId, rw -> rw));

            List<RepWeight> updatedRepWeights = new ArrayList<>();

            for (RepWeight newRepWeight : newRepWeights) {
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
    @CacheEvict(value = {"exercises", "exercisesByMember", "exerciseAll"}, allEntries = true)
    public void delete(Long id) {
        Exercise exercise = findById(id);
        exerciseRepository.delete(exercise);
    }

    @Cacheable(value = "exercisesByMember", key = "#memberId")
    public List<ExerciseResponseDto> fetchMemberExercises(Long memberId) {
        List<Exercise> exercises = exerciseRepository.findExercisesByMemberId(memberId);

        return exercises.stream()
                .map(ExerciseResponseDto::new)
                .collect(Collectors.toList());
    }

    public Exercise fetchById(Long id) {
        return exerciseRepository.fetchById(id);
    }

    @Cacheable(value = "exercises", key = "#id")
    public ExerciseResponseDto fetchOne(Long id){
        Exercise findExercise = exerciseRepository.fetchById(id);
        return new ExerciseResponseDto(findExercise);
    }

    @Cacheable(value = "exerciseAll")
    public List<ExerciseResponseDto> fetchAll() {
        List<Exercise> exercises = exerciseRepository.fetchAll();
        return exercises.stream()
                .map(ExerciseResponseDto::new)
                .collect(Collectors.toList());

    }

    @Transactional
    @CacheEvict(value = {"exercises", "exercisesByMember", "exerciseAll"}, allEntries = true)
    public void deleteById(Long id) {
        exerciseRepository.deleteById(id);
    }
}
