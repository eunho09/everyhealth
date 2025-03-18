package com.example.everyhealth.repository;

import com.example.everyhealth.domain.RoutineExercise;
import com.example.everyhealth.dto.RoutineExerciseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, Long> {

    @Query("select re from RoutineExercise re join re.routine r join fetch re.exercise e where re.routine.id =:routineId order by re.sequence asc")
    List<RoutineExercise> findRoutineExerciseByRoutineId(@Param("routineId") Long routineId);

    @Query("select re from RoutineExercise re join re.routine r join fetch re.exercise e join fetch re.repWeightList where e.member.id =:memberId")
    List<RoutineExercise> findRoutineExerciseByMemberId(@Param("memberId") Long memberId);

    @Query("select re from RoutineExercise re join fetch re.exercise join fetch re.repWeightList where re.routine.id=:routineId order by re.sequence asc")
    List<RoutineExercise> findByRoutineId(@Param("routineId") Long routineId);

    @Query("select re from RoutineExercise re join fetch re.exercise join fetch re.repWeightList join fetch re.routine r where r.id=:routineId order by re.sequence asc")
    List<RoutineExercise> findAllByRoutineIdWithExerciseAndRepWeight(@Param("routineId") Long routineId);

}
