package com.example.everyhealth.controller;

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

    private final JwtTokenGenerator jwtTokenGenerator;
    private final MemberService memberService;
    private final FriendService friendService;

    @GetMapping("/member/friend/request")
    public ResponseEntity<List<FriendDto>> findByFriendIdAndStatus(@CookieValue(name = "jwt") String token) {
        Long memberId = jwtTokenGenerator.getUserId(token);
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

    @GetMapping("/member/friend")
    public ResponseEntity<List<FriendDto>> findMyFriend(@CookieValue(name = "jwt") String token) {
        Long memberId = jwtTokenGenerator.getUserId(token);
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
    @PostMapping("/friend/request/{friendMemberId}")
    public ResponseEntity<String> requestFriendShip(@CookieValue(name = "jwt") String token ,@PathVariable Long friendMemberId) {
        Long memberId = jwtTokenGenerator.getUserId(token);
        Member member = memberService.findById(memberId);
        Friend friend = new Friend(member);

        Member friendMember = memberService.findById(friendMemberId);
        friend.setFriend(friendMember);
        friend.setStatus(FriendShip.REQUEST);
        friendService.save(friend);

        return ResponseEntity.ok("친구 요청");
    }

    //요청이 온 회원을 친구로 수락
    @PostMapping("/friend/accept/{memberId}")
    public ResponseEntity<String> acceptFriendShip(@CookieValue(name = "jwt") String token, @PathVariable Long memberId) {
        Long friendMemberId = jwtTokenGenerator.getUserId(token);
        friendService.selectRequest(memberId, friendMemberId, FriendShip.ACCEPT);

        return ResponseEntity.ok("친구 수락");
    }

    //요청이 온 회원을 친구거절
    @PostMapping("/friend/cancel/{friendMemberId}")
    public ResponseEntity<String> cancelFriendShip(@CookieValue(name = "jwt") String token, @PathVariable Long friendMemberId) {
        Long memberId = jwtTokenGenerator.getUserId(token);
        friendService.selectRequest(memberId, friendMemberId, FriendShip.CANCEL);

        return ResponseEntity.ok("친구 거절");
    }

    @GetMapping("/member/suggested-friends")
    public ResponseEntity<List<MemberDto>> getSuggestedFriends(@CookieValue(name = "jwt") String token) {
        Long memberId = jwtTokenGenerator.getUserId(token);
        List<MemberDto> suggestedFriends = memberService.findSuggestedFriend(memberId);
        return ResponseEntity.ok(suggestedFriends);
    }
}
