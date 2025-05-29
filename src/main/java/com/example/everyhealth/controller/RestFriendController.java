package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.FriendShip;
import com.example.everyhealth.dto.FriendDto;
import com.example.everyhealth.dto.MemberDto;
import com.example.everyhealth.service.FriendBusinessService;
import com.example.everyhealth.service.FriendDataService;
import com.example.everyhealth.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestFriendController {

    private final MemberService memberService;
    private final FriendDataService friendDataService;
    private final FriendBusinessService friendBusinessService;


    @GetMapping("/member/friend/request")
    public ResponseEntity<List<FriendDto>> findByFriendIdAndStatus(@ExtractMemberId Long memberId) {
        List<FriendDto> response = friendDataService.findByFriendIdAndStatus(memberId, FriendShip.REQUEST);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/member/friend")
    public ResponseEntity<List<FriendDto>> findMyFriend(@ExtractMemberId Long memberId) {
        List<FriendDto> response = friendDataService.findMyFriend(memberId);

        return ResponseEntity.ok(response);
    }

    //친구 요청을 보냄
    @PostMapping("/friend/request/{friendMemberId}")
    public ResponseEntity<String> requestFriendShip(@ExtractMemberId Long memberId ,@PathVariable Long friendMemberId) {
        friendBusinessService.sendRequestFriend(memberId, friendMemberId);

        return ResponseEntity.ok("친구 요청");
    }

    //요청이 온 회원을 친구로 수락
    @PostMapping("/friend/accept/{memberId}")
    public ResponseEntity<String> acceptFriendShip(@ExtractMemberId Long friendMemberId, @PathVariable Long memberId) {
        friendBusinessService.requestAcceptFriend(memberId, friendMemberId);

        return ResponseEntity.ok("친구 수락");
    }

    //요청이 온 회원을 친구거절
    @PostMapping("/friend/cancel/{friendMemberId}")
    public ResponseEntity<String> cancelFriendShip(@ExtractMemberId Long memberId, @PathVariable Long friendMemberId) {
        friendBusinessService.acceptCancelFriend(memberId, friendMemberId);

        return ResponseEntity.ok("친구 거절");
    }


    @GetMapping("/member/suggested-friends")
    public ResponseEntity<List<MemberDto>> getSuggestedFriends(@ExtractMemberId Long memberId) {
        List<MemberDto> suggestedFriends = memberService.findSuggestedFriend(memberId);
        return ResponseEntity.ok(suggestedFriends);
    }


    @GetMapping("/friend/check/{friendId}")
    public ResponseEntity<Boolean> checkAcceptFriendShip(@PathVariable Long friendId, @ExtractMemberId Long memberId) {
        boolean b = friendDataService.checkAcceptFriendShip(friendId, memberId);
        return ResponseEntity.ok(b);
    }
}
