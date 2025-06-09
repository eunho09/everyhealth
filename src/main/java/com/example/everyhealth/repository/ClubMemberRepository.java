package com.example.everyhealth.repository;

import com.example.everyhealth.domain.ClubMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    @Query("select exists (select 1 from ClubMember cm where cm.member.id =:memberId)")
    boolean existsByMemberId(@Param("memberId") Long memberId);

    @Query("select cm from ClubMember cm where cm.member.id =:memberId and cm.club.id =:clubId")
    ClubMember findByMemberIdAndClubId(@Param("memberId") Long memberId, @Param("clubId") Long clubId);

    @Query("select cm from ClubMember cm where cm.member.id=:memberId and cm.club.id =(SELECT cr.club.id FROM ChatRoom cr WHERE cr.id = :chatRoomId)")
    ClubMember findByMemberIdAndChatRoomId(@Param("memberId") Long memberId, @Param("chatRoomId") Long chatRoomId);

    @Modifying
    @Query("delete from ClubMember cm where cm.club.id=:clubId")
    void deleteByClubId(@Param("clubId") Long clubId);

    @Modifying
    @Query("delete from ClubMember cm where cm.club.id=:clubId and cm.member.id=:memberId")
    void deleteByClubIdAndMemberId(@Param("clubId") Long clubId, @Param("memberId") Long memberId);
}
