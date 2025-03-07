package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.loginId=:loginId")
    Member findByLoginId(@Param("loginId") String loginId);

    @Query("SELECT new com.example.everyhealth.dto.MemberDto(m.id, m.name, m.picture) " +
            "FROM Member m " +
            "WHERE m.id NOT IN :memberId " +
            "AND m.id NOT IN (SELECT f.friend.id FROM Friend f WHERE f.member.id = :memberId) " +
            "ORDER BY RAND()")
    List<MemberDto> findSuggestedFriend(@Param("memberId") Long memberId);

    @Query("select m from Member m where m.id=(select f.friend.id from Friend f where f.id=:friendId)")
    Member findByFriendInfo(@Param("friendId") Long friendId);
}
