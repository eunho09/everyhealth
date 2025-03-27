package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.loginId=:loginId")
    Member findByLoginId(@Param("loginId") String loginId);

    @Query("SELECT new com.example.everyhealth.dto.MemberDto(m) " +
            "FROM Member m " +
            "WHERE m.id NOT IN :memberId " +
            "AND m.id NOT IN (SELECT f.friend.id FROM Friend f WHERE f.member.id = :memberId) " +
            "ORDER BY RAND()")
    List<MemberDto> findSuggestedFriend(@Param("memberId") Long memberId);

    @Query("select m.id from Member m where m.id=:friendId")
    Long findIdByFriendId(@Param("friendId") Long friendId);

    @Query("select m from Member m where m.id=:friendId")
    Member findByFriendId(@Param("friendId") Long friendId);

    @Query("select exists (select 1 from Member m join m.friendList f where m.id=:id and f.friend.id =:friendId and f.status=com.example.everyhealth.domain.FriendShip.ACCEPT)")
    boolean existsByIdAndFriendId(@Param("id") Long id, @Param("friendId") Long friendId);

    @Query("select m from Member m where m.name=:name")
    Optional<Member> findByName(@Param("name") String name);
}
