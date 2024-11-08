package com.example.everyhealth.repository;

import com.example.everyhealth.domain.TodayExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodayExerciseRepository extends JpaRepository<TodayExercise, Long> {
}
