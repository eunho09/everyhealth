package com.example.everyhealth.repository;

import com.example.everyhealth.domain.RoutineExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, Long> {

    @Query("select re from RoutineExercise re join fetch re.routine r join fetch re.exercise e join fetch e.repWeightList where re.routine.id =:routineId order by re.sequence asc")
    List<RoutineExercise> fetchRoutineExerciseByRoutineId(@Param("routineId") Long routineId);

    @Query("select re from RoutineExercise re join fetch re.exercise join fetch re.repWeightList where re.routine.id=:routineId order by re.sequence asc")
    List<RoutineExercise> findByRoutineId(@Param("routineId") Long routineId);

    @Query("select re from RoutineExercise re join fetch re.exercise join fetch re.repWeightList join fetch re.routine r where r.id=:routineId order by re.sequence asc")
    List<RoutineExercise> findAllByRoutineIdWithExerciseAndRepWeight(@Param("routineId") Long routineId);

    @Modifying
    @Query("delete from RoutineExercise re where re.id=:routineExerciseId")
    void deleteById(@Param("routineExerciseId") Long routineExerciseId);

    @Modifying
    @Query("delete from RoutineExercise re where re.routine.id=:routineId")
    void deleteByRoutineId(@Param("routineId") Long routineId);

    @Query("select re from RoutineExercise re left join fetch re.exercise left join fetch re.repWeightList where re.routine.id in :routineIds")
    List<RoutineExercise> fetchByRoutineIds(@Param("routineIds") List<Long> routineIds);

}
