package com.example.everyhealth.repository;

import com.example.everyhealth.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select c from ChatRoom c where c.club.id=:clubId")
    ChatRoom findByClubId(@Param("clubId") Long clubId);

    @Query("select c from ChatRoom c where c.club.id in (select cm.club.id from ClubMember cm where cm.member.id = :memberId)")
    List<ChatRoom> fetchByMemberId(@Param("memberId") Long memberId);
}


