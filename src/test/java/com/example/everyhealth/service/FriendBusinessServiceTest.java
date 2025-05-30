package com.example.everyhealth.service;

import com.example.everyhealth.domain.Friend;
import com.example.everyhealth.domain.FriendShip;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.MemberRole;
import com.example.everyhealth.exception.FriendException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
@TestPropertySource(locations = "file:C:/coding/spring/project/everyhealth/src/main/resources/.env-local")
class FriendBusinessServiceTest {

    @Autowired
    FriendBusinessService friendBusinessService;

    @Autowired
    FriendDataService friendDataService;

    @Autowired
    MemberService memberService;

    private Long memberId;
    private Long member2Id;
    private Member member;
    private Member member2;

    @BeforeEach
    void init() {
        // 멤버 생성 및 저장
        member = new Member("길동", "test1", MemberRole.USER, "test1", "test1");
        memberId = memberService.save(member);

        member2 = new Member("홍길", "test2", MemberRole.USER, "test2", "test2");
        member2Id = memberService.save(member2);
    }

    @Test
    @DisplayName("친구 요청")
    void sendRequest(){
        Long friendId = friendBusinessService.sendRequestFriend(memberId, member2Id);
        Friend friend = friendDataService.findById(friendId);

        org.assertj.core.api.Assertions.assertThat(friend.getMember()).isEqualTo(memberId);
        org.assertj.core.api.Assertions.assertThat(friend.getFriend()).isEqualTo(member2Id);
        org.assertj.core.api.Assertions.assertThat(friend.getStatus()).isEqualTo(FriendShip.REQUEST);
    }

    @Test
    @DisplayName("친구요청 중 중복 요청")
    void sendRequestDuplicate() {
        friendBusinessService.sendRequestFriend(memberId, member2Id);

        Assertions.assertThrows(FriendException.class, () -> {
            friendBusinessService.sendRequestFriend(memberId, member2Id);
        });
    }

    @Test
    @DisplayName("친구요청 중 이미 친구")
    void sendRequestFriend() {
        friendBusinessService.sendRequestFriend(memberId, member2Id);
        friendBusinessService.requestAcceptFriend(memberId, member2Id);

        Assertions.assertThrows(FriendException.class, () -> {
            friendBusinessService.sendRequestFriend(memberId, member2Id);
        });
    }

    @Test
    @DisplayName("친구 요청 받기")
    void acceptRequest(){
        friendBusinessService.sendRequestFriend(memberId, member2Id);
        Long friendId = friendBusinessService.requestAcceptFriend(memberId, member2Id);
        Friend friend = friendDataService.findById(friendId);

        org.assertj.core.api.Assertions.assertThat(friend.getMember().getId()).isEqualTo(memberId);
        org.assertj.core.api.Assertions.assertThat(friend.getFriend().getId()).isEqualTo(member2Id);
        org.assertj.core.api.Assertions.assertThat(friend.getStatus()).isEqualTo(FriendShip.ACCEPT);
    }

    @Test
    @DisplayName("친구요청 받기 중 이미 친구")
    void acceptAlreadyFriend() {
        friendBusinessService.sendRequestFriend(memberId, member2Id);
        friendBusinessService.requestAcceptFriend(memberId, member2Id);

        Assertions.assertThrows(FriendException.class, () -> {
            friendBusinessService.requestAcceptFriend(memberId, member2Id);
        });
    }

    @Test
    @DisplayName("친구요청이 안 온 상태")
    void acceptNotRequest() {
        Assertions.assertThrows(FriendException.class, () -> {
            friendBusinessService.requestAcceptFriend(memberId, member2Id);
        });
    }

    @Test
    @DisplayName("친구 요청 취소")
    void acceptCancel() {
        friendBusinessService.sendRequestFriend(memberId, member2Id);
        Long friendId = friendBusinessService.acceptCancelFriend(memberId, member2Id);
        Friend friend = friendDataService.findById(friendId);

        org.assertj.core.api.Assertions.assertThat(friend.getMember().getId()).isEqualTo(memberId);
        org.assertj.core.api.Assertions.assertThat(friend.getFriend().getId()).isEqualTo(member2Id);
        org.assertj.core.api.Assertions.assertThat(friend.getStatus()).isEqualTo(FriendShip.CANCEL);
    }

    @Test
    @DisplayName("친구 요청 취소 중 이미 친구")
    void acceptCancelAlreadyFriend() {
        friendBusinessService.sendRequestFriend(memberId, member2Id);
        friendBusinessService.requestAcceptFriend(memberId, member2Id);

        Assertions.assertThrows(FriendException.class, () -> {
            friendBusinessService.acceptCancelFriend(memberId, member2Id);
        });
    }
}