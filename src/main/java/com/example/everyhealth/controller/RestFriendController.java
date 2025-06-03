package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.FriendShip;
import com.example.everyhealth.dto.FriendDto;
import com.example.everyhealth.dto.MemberDto;
import com.example.everyhealth.service.FriendBusinessService;
import com.example.everyhealth.service.FriendDataService;
import com.example.everyhealth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "친구 관리")
public class RestFriendController {

    private final MemberService memberService;
    private final FriendDataService friendDataService;
    private final FriendBusinessService friendBusinessService;


    @GetMapping("/member/friend/request")
    @Operation(summary = "친구 요청 중인 목록")
    public ResponseEntity<List<FriendDto>> findByFriendIdAndStatus(@ExtractMemberId Long memberId) {
        List<FriendDto> response = friendDataService.findByFriendIdAndStatus(memberId, FriendShip.REQUEST);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/member/friend")
    @Operation(summary = "나의 친구 목록")
    public ResponseEntity<List<FriendDto>> findMyFriend(@ExtractMemberId Long memberId) {
        List<FriendDto> response = friendDataService.findMyFriend(memberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/friend/request/{friendMemberId}")
    @Operation(summary = "친구 요청 보내기")
    public ResponseEntity<String> requestFriendShip(@ExtractMemberId Long memberId ,@PathVariable Long friendMemberId) {
        friendBusinessService.sendRequestFriend(memberId, friendMemberId);

        return ResponseEntity.ok("친구 요청");
    }

    @PostMapping("/friend/accept/{memberId}")
    @Operation(summary = "친구 수락")
    public ResponseEntity<String> acceptFriendShip(@ExtractMemberId Long friendMemberId, @PathVariable Long memberId) {
        friendBusinessService.requestAcceptFriend(memberId, friendMemberId);

        return ResponseEntity.ok("친구 수락");
    }

    @PostMapping("/friend/cancel/{friendMemberId}")
    @Operation(summary = "친구 거절")
    public ResponseEntity<String> cancelFriendShip(@ExtractMemberId Long memberId, @PathVariable Long friendMemberId) {
        friendBusinessService.acceptCancelFriend(memberId, friendMemberId);

        return ResponseEntity.ok("친구 거절");
    }


    @GetMapping("/member/suggested-friends")
    @Operation(summary = "친구 추천")
    public ResponseEntity<List<MemberDto>> getSuggestedFriends(@ExtractMemberId Long memberId) {
        List<MemberDto> suggestedFriends = memberService.findSuggestedFriend(memberId);
        return ResponseEntity.ok(suggestedFriends);
    }


    @GetMapping("/friend/check/{friendId}")
    @Operation(summary = "친구상태 확인")
    public ResponseEntity<Boolean> checkAcceptFriendShip(@PathVariable Long friendId, @ExtractMemberId Long memberId) {
        boolean b = friendDataService.checkAcceptFriendShip(friendId, memberId);
        return ResponseEntity.ok(b);
    }
}
