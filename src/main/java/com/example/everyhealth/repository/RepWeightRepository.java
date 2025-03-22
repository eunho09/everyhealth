package com.example.everyhealth.repository;

import com.example.everyhealth.domain.RepWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepWeightRepository extends JpaRepository<RepWeight, Long> {

    @Modifying
    @Query("delete from RepWeight re where re.routineExercise.routine.id =:routineId")
    void deleteByRoutineId(@Param("routineId") Long routineId);

    @Modifying
    @Query("delete from RepWeight re where re.routineExercise.id =:routineExerciseId")
    void deleteByRoutineExerciseId(@Param("routineExerciseId") Long routineExerciseId);

    @Modifying
    @Query("delete from RepWeight re where re.exercise.id =:exerciseId")
    void deleteByExerciseId(@Param("exerciseId") Long exerciseId);

    @Modifying
    @Query("delete from RepWeight re where re.todayExercise.id =:todayExerciseId")
    void deleteByTodayExerciseId(@Param("todayExerciseId") Long todayExerciseId);
}

