package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Friend;
import com.example.everyhealth.domain.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f from Friend f where f.friend.id=:friendMemberId and f.status=:status")
    List<Friend> findByFriendIdAndStatus(@Param("friendMemberId") Long friendMemberId, @Param("status") FriendShip status);

    @Query("select f from Friend f join fetch f.friend join fetch f.member m where m.id=:memberId and f.status=com.example.everyhealth.domain.FriendShip.ACCEPT")
    List<Friend> findMyFriend(@Param("memberId") Long memberId);

    @Query("select f.status from Friend f where f.member.id=:memberId and f.friend.id=:friendId")
    FriendShip checkFriendShip(@Param("memberId") Long memberId, @Param("friendId") Long friendId);

    @Query("select f from Friend f join fetch f.member join fetch f.friend where f.id=:id")
    Friend fetchByIdWithMemberAndFriend(@Param("id") Long id);

    @Query("select f from Friend f where f.member.id=:memberId and f.friend.id=:friendMemberId and f.status=:status")
    Friend findByMemberIdAndFriendIdAndStatus(@Param("memberId") Long memberId, @Param("friendMemberId") Long friendMemberId, @Param("status") FriendShip status);
}
