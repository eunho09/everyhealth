package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.dto.ExerciseDto;
import com.example.everyhealth.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Exercise exercise = findById(id);
        if (dto.getName() != null) {
            exercise.setName(dto.getName());
        }
        if (dto.getMemo() != null) {
            exercise.setMemo(dto.getMemo());
        }
        if (dto.getClassification() != null) {
            exercise.setClassification(dto.getClassification());
        }
        if (dto.getRepWeight() != null) {
            exercise.setRepWeight(dto.getRepWeight());
        }
    }

    //만약 다른 연관관계가 걸려있다면 에러를 발생해야하는데 어떤 에러인줄 모르겠어
    public void delete(Long id) {
        Exercise exercise = findById(id);
        exerciseRepository.delete(exercise);
    }

    public List<Exercise> findExercisesByMemberId(Long id) {
        return exerciseRepository.findExercisesByMemberId(id);
    }
}
