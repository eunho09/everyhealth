package com.example.everyhealth.service;

import com.example.everyhealth.domain.Friend;
import com.example.everyhealth.domain.FriendShip;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.repository.FriendRepository;
import com.example.everyhealth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final FriendRepository friendRepository;

    @Transactional
    public Long save(Friend friend) {
        Friend saveFriend = friendRepository.save(friend);
        return saveFriend.getId();
    }

    public Friend findById(Long id) {
        return friendRepository.findById(id).get();
    }

    public List<Friend> findAll() {
        return friendRepository.findAll();
    }

    public List<Friend> findByMemberId(Long memberId) {
        return friendRepository.findByMemberId(memberId);
    }

    public List<Friend> findByFriendIdAndStatus(Long friendMemberId, FriendShip status) {
        return friendRepository.findByFriendIdAndStatus(friendMemberId, status);
    }

    public Friend findByMemberIdAndFriendIdAndStatus(Long memberId, Long friendMemberId, FriendShip status) {
        return friendRepository.findByMemberIdAndFriendIdAndStatus(memberId, friendMemberId, status);
    }

    public List<Friend> findMyFriend(Long memberId) {
        return friendRepository.findMyFriend(memberId);
    }

    @Transactional
    public void selectRequest(Long memberId, Long friendMemberId, FriendShip friendShip) {
        Friend friend = friendRepository.findByMemberIdAndFriendIdAndStatus(memberId, friendMemberId, FriendShip.REQUEST);
        friend.setStatus(friendShip);
    }

    public boolean checkFriendShip(Long friendId, Long memberId) {
        Friend friend = friendRepository.checkFriendShip(friendId, memberId);
        return friend != null;
    }

    public Friend findByFriendIdAndMemberId(Long friendId, Long memberId) {
        return friendRepository.fetchByFriendIdAndMemberId(friendId, memberId);
    }
}
