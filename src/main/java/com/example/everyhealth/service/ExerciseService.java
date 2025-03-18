package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.domain.RepWeight;
import com.example.everyhealth.dto.ExerciseDto;
import com.example.everyhealth.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
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
    public void update(Long id, ExerciseDto dto) {
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

    public void delete(Long id) {
        Exercise exercise = findById(id);
        exerciseRepository.delete(exercise);
    }

    public List<Exercise> findExercisesByMemberId(Long id) {
        return exerciseRepository.findExercisesByMemberId(id);
    }

    public Exercise fetchById(Long id) {
        return exerciseRepository.fetchById(id);
    }

    public List<Exercise> fetchAll(){
        return exerciseRepository.fetchAll();
    }
}
