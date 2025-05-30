package com.example.everyhealth.service;

import com.example.everyhealth.domain.Friend;
import com.example.everyhealth.domain.FriendShip;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.exception.ErrorCode;
import com.example.everyhealth.exception.FriendException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FriendBusinessService {

    private final FriendDataService friendDataService;
    private final MemberService memberService;

    @Transactional
    public Long sendRequestFriend(Long memberId, Long friendMemberId){
        validateId(memberId, friendMemberId);

        FriendShip friendShip = friendDataService.chekFriendShip(memberId, friendMemberId);
        if (friendShip == FriendShip.REQUEST){
            throw new FriendException(ErrorCode.FRIEND_ALREADY_SEND);
        }
        if (friendShip == FriendShip.ACCEPT){
            throw new FriendException(ErrorCode.FRIEND_ALREADY_ACCEPT);
        }

        Member member = memberService.findById(memberId);
        Friend friend = new Friend(member);

        Member friendMember = memberService.findById(friendMemberId);
        friend.setFriend(friendMember);
        friend.setStatus(FriendShip.REQUEST);
        friendDataService.save(friend);

        return friend.getId();
    }

    @Transactional
    public Long requestAcceptFriend(Long memberId, Long friendMemberId) {
        validateId(memberId, friendMemberId);
        FriendShip friendShip = friendDataService.chekFriendShip(memberId, friendMemberId);
        if (friendShip == FriendShip.ACCEPT){
            throw new FriendException(ErrorCode.FRIEND_ALREADY_ACCEPT);
        }

        if (friendShip == FriendShip.CANCEL || friendShip == null){
            throw new FriendException(ErrorCode.FRIEND_NOT_REQUEST);
        }

        return friendDataService.selectRequest(memberId, friendMemberId, FriendShip.ACCEPT);
    }

    @Transactional
    public Long acceptCancelFriend(Long memberId, Long friendMemberId) {
        validateId(memberId, friendMemberId);
        FriendShip friendShip = friendDataService.chekFriendShip(memberId, friendMemberId);

        if (friendShip != FriendShip.REQUEST){
            throw new FriendException(ErrorCode.FRIEND_NOT_ACCEPT);
        }

        return friendDataService.selectRequest(memberId, friendMemberId, FriendShip.CANCEL);
    }

    private static void validateId(Long memberId, Long friendMemberId) {
        if (memberId == null || friendMemberId == null){
            throw new FriendException(ErrorCode.FRIEND_NULL_ID, memberId, friendMemberId);
        }

        if (memberId.equals(friendMemberId)){
            throw new FriendException(ErrorCode.FRIEND_SAME_ID, memberId, friendMemberId);
        }
    }
}
