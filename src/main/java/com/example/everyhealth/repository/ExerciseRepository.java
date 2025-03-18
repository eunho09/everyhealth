package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Query("select e from Exercise e join fetch e.repWeightList where e.member.id =:memberId")
    List<Exercise> findExercisesByMemberId(@Param("memberId") Long memberId);

    @Query("select e from Exercise e join fetch e.repWeightList where e.id=:exerciseId")
    Exercise fetchById(@Param("exerciseId") Long exerciseId);
}
