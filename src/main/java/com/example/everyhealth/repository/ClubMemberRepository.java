package com.example.everyhealth.repository;

import com.example.everyhealth.domain.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    @Query("select cm from ClubMember cm where cm.member.id =:memberId")
    public ClubMember existsByMemberId(@Param("memberId") Long memberId);

    @Query("select cm from ClubMember cm where cm.member.id =:memberId and cm.club.id =:clubId")
    public ClubMember findByMemberIdAndClubId(@Param("memberId") Long memberId, @Param("clubId") Long clubId);

    @Query("select cm from ClubMember cm where cm.member.id=:memberId and cm.club.chatRoom.id=:chatRoomId")
    public ClubMember findByMemberIdAndChatRoomId(@Param("memberId") Long memberId, @Param("chatRoomId") Long chatRoomId);
}
