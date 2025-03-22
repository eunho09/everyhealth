package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long>, JpaSpecificationExecutor<Club> {

    @Query("select c from Club c where c.chatRoom.id=:chatRoomId")
    Club findByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    @Modifying
    @Query("delete from Club c where c.id =:id")
    void deleteById(@Param("id") Long id);
}
