package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    @Query("select r from Routine r left join fetch r.routineExerciseList re left join fetch re.exercise e where r.member.id =:memberId order by re.sequence asc")
    List<Routine> findRoutineWithExercises(@Param("memberId") Long memberId);
}
