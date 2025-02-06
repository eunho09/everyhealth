package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.Friend;
import com.example.everyhealth.domain.FriendShip;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.FriendDto;
import com.example.everyhealth.dto.MemberDto;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.FriendService;
import com.example.everyhealth.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestFriendController {

    private final MemberService memberService;
    private final FriendService friendService;

    @ExtractMemberId
    @GetMapping("/member/friend/request")
    public ResponseEntity<List<FriendDto>> findByFriendIdAndStatus(Long memberId) {
        List<Friend> requestFriendList = friendService.findByFriendIdAndStatus(memberId, FriendShip.REQUEST);
        List<FriendDto> collect = requestFriendList.stream()
                .map(f -> new FriendDto(f.getId(),
                        new MemberDto(f.getMember().getId(),
                                f.getMember().getName(),
                                f.getMember().getPicture()),
                        new MemberDto(f.getFriend().getId(),
                                f.getFriend().getName(),
                                f.getFriend().getPicture())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    @ExtractMemberId
    @GetMapping("/member/friend")
    public ResponseEntity<List<FriendDto>> findMyFriend(Long memberId) {
        List<Friend> friendList = friendService.findMyFriend(memberId);

        List<FriendDto> collect = friendList.stream()
                .map(f -> new FriendDto(f.getId(),
                        new MemberDto(f.getMember().getId(),
                                f.getMember().getName(),
                                f.getMember().getPicture()),
                        new MemberDto(f.getFriend().getId(),
                                f.getFriend().getName(),
                                f.getFriend().getPicture())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    //친구요청을 보냄
    @ExtractMemberId
    @PostMapping("/friend/request/{friendMemberId}")
    public ResponseEntity<String> requestFriendShip(Long memberId ,@PathVariable Long friendMemberId) {
        Member member = memberService.findById(memberId);
        Friend friend = new Friend(member);

        Member friendMember = memberService.findById(friendMemberId);
        friend.setFriend(friendMember);
        friend.setStatus(FriendShip.REQUEST);
        friendService.save(friend);

        return ResponseEntity.ok("친구 요청");
    }

    //요청이 온 회원을 친구로 수락
    @ExtractMemberId
    @PostMapping("/friend/accept/{memberId}")
    public ResponseEntity<String> acceptFriendShip(Long friendMemberId, @PathVariable Long memberId) {
        friendService.selectRequest(memberId, friendMemberId, FriendShip.ACCEPT);

        return ResponseEntity.ok("친구 수락");
    }

    //요청이 온 회원을 친구거절
    @ExtractMemberId
    @PostMapping("/friend/cancel/{friendMemberId}")
    public ResponseEntity<String> cancelFriendShip(Long memberId, @PathVariable Long friendMemberId) {
        friendService.selectRequest(memberId, friendMemberId, FriendShip.CANCEL);

        return ResponseEntity.ok("친구 거절");
    }

    @ExtractMemberId
    @GetMapping("/member/suggested-friends")
    public ResponseEntity<List<MemberDto>> getSuggestedFriends(Long memberId) {
        List<MemberDto> suggestedFriends = memberService.findSuggestedFriend(memberId);
        return ResponseEntity.ok(suggestedFriends);
    }
}
