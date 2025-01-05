package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.member.id=:memberId")
    List<Post> findByMemberId(@Param("memberId") Long memberId);
}
