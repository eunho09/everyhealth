package com.example.everyhealth.service;

import com.example.everyhealth.domain.Friend;
import com.example.everyhealth.domain.FriendShip;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.FriendDto;
import com.example.everyhealth.dto.MemberDto;
import com.example.everyhealth.exception.ErrorCode;
import com.example.everyhealth.exception.FriendException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FriendBusinessService {

    private final FriendDataService friendDataService;
    private final MemberService memberService;

    @Transactional
    public Long sendRequestFriend(Long memberId, Long friendMemberId){
        validateId(memberId, friendMemberId);

        FriendShip friendShip = friendDataService.checkFriendShip(memberId, friendMemberId);
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
        FriendShip friendShip = friendDataService.checkFriendShip(memberId, friendMemberId);
        if (friendShip == FriendShip.ACCEPT){
            throw new FriendException(ErrorCode.FRIEND_ALREADY_ACCEPT);
        }

        if (friendShip == FriendShip.CANCEL || friendShip == null){
            throw new FriendException(ErrorCode.FRIEND_NOT_REQUEST);
        }

        return selectRequest(memberId, friendMemberId, friendShip, FriendShip.ACCEPT);
    }

    @Transactional
    public Long acceptCancelFriend(Long memberId, Long friendMemberId) {
        validateId(memberId, friendMemberId);
        FriendShip friendShip = friendDataService.checkFriendShip(memberId, friendMemberId);

        if (friendShip != FriendShip.REQUEST){
            throw new FriendException(ErrorCode.FRIEND_NOT_ACCEPT);
        }

        return selectRequest(memberId, friendMemberId, friendShip, FriendShip.CANCEL);
    }

    private static void validateId(Long memberId, Long friendMemberId) {
        if (memberId == null || friendMemberId == null){
            throw new FriendException(ErrorCode.FRIEND_NULL_ID, memberId, friendMemberId);
        }

        if (memberId.equals(friendMemberId)){
            throw new FriendException(ErrorCode.FRIEND_SAME_ID, memberId, friendMemberId);
        }
    }

    @Cacheable(value = "friendByAccept", key = "#friendMemberId")
    public List<FriendDto> findByFriendIdAndStatus(Long friendMemberId, FriendShip status) {
        List<Friend> requestFriendList = friendDataService.findByFriendIdAndStatus(friendMemberId, status);
        return requestFriendList.stream()
                .map(f -> new FriendDto(f.getId(),
                        new MemberDto(f.getMember()),
                        new MemberDto(f.getFriend())))
                .collect(Collectors.toList());
    }



    @Cacheable(value = "friendByMember", key = "#memberId")
    public List<FriendDto> getFriendByMemberIdDtos(Long memberId) {
        List<Friend> friendList = friendDataService.findMyFriend(memberId);

        return friendList.stream()
                .map(f -> new FriendDto(f.getId(),
                        new MemberDto(f.getMember()),
                        new MemberDto(f.getFriend())))
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = {"friendShipCheck", "friendByAccept", "friendByMember", "memberByFriend", "existsByIdAndFriendId"}, allEntries = true)
    public Long selectRequest(Long memberId, Long friendMemberId, FriendShip status, FriendShip updateStatus) {
        Friend friend = friendDataService.findByMemberIdAndFriendIdAndStatus(memberId, friendMemberId, status);
        friend.setStatus(updateStatus);

        return friend.getId();
    }

    @Cacheable(value = "friendShipCheck", key = "#memberId")
    public boolean checkAcceptFriendShip(Long friendId, Long memberId) {
        FriendShip friend = friendDataService.checkFriendShip(friendId, memberId);
        return friend == FriendShip.ACCEPT;
    }
}
