package com.example.everyhealth.service;

import com.example.everyhealth.domain.Exercise;
import com.example.everyhealth.repository.ExerciseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseDataService {

    private final ExerciseRepository exerciseRepository;

    @Transactional
    @CacheEvict(value = {"exercisesByMember", "exerciseAll"}, allEntries = true)
    public Long save(Exercise exercise) {
        exerciseRepository.save(exercise);
        return exercise.getId();
    }

    public Exercise findById(Long id) {
        return exerciseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 운동이 존재하지 않습니다. ID : " + id));
    }

    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    public List<Exercise> fetchAll() {
        return exerciseRepository.fetchAll();
    }

    public List<Exercise> findExercisesByMemberId(Long memberId) {
        return exerciseRepository.findExercisesByMemberId(memberId);
    }

    public Exercise fetchById(Long id) {
        return exerciseRepository.fetchById(id);
    }

    @Transactional
    @CacheEvict(value = {"exercises", "exercisesByMember", "exerciseAll"}, allEntries = true)
    public void delete(Long id) {
        Exercise exercise = findById(id);
        exerciseRepository.delete(exercise);
    }

    @Transactional
    @CacheEvict(value = {"exercises", "exercisesByMember", "exerciseAll"}, allEntries = true)
    public void deleteById(Long id) {
        exerciseRepository.deleteById(id);
    }
}
