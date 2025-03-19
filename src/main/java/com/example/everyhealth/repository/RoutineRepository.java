package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    @Query("select r from Routine r left join fetch r.routineExerciseList re where r.member.id =:memberId order by re.sequence asc")
    List<Routine> fetchByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("delete from Routine r where r.id=:routineId")
    void deleteById(@Param("routineId") Long routineId);
}
