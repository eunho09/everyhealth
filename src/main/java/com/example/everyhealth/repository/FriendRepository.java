package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Friend;
import com.example.everyhealth.domain.FriendShip;
import com.example.everyhealth.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f from Friend f where f.member.id=:memberId")
    List<Friend> findByMemberId(@Param("memberId") Long memberId);

    @Query("select f from Friend f where f.friend.id=:friendMemberId and f.status=:status")
    List<Friend> findByFriendIdAndStatus(@Param("friendMemberId") Long friendMemberId, @Param("status") FriendShip status);

    @Query("select f from Friend f join fetch f.friend where f.member.id=:memberId and f.status=com.example.everyhealth.domain.FriendShip.ACCEPT")
    List<Friend> findMyFriend(@Param("memberId") Long memberId);

    @Query("select f from Friend f where f.member.id=:memberId and f.friend.id=:friendMemberId and f.status=:status")
    Friend findByMemberIdAndFriendIdAndStatus(@Param("memberId") Long memberId, @Param("friendMemberId") Long friendMemberId, @Param("status") FriendShip status);

}
