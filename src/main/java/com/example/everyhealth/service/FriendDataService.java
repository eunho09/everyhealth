package com.example.everyhealth.service;

import com.example.everyhealth.domain.Friend;
import com.example.everyhealth.domain.FriendShip;
import com.example.everyhealth.dto.FriendDto;
import com.example.everyhealth.dto.MemberDto;
import com.example.everyhealth.repository.FriendRepository;
import com.example.everyhealth.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendDataService {

    private final FriendRepository friendRepository;

    @Transactional
    @CacheEvict(value = {"friendShipCheck", "friendByAccept", "friendByMember"}, allEntries = true)
    public Long save(Friend friend) {
        Friend saveFriend = friendRepository.save(friend);
        return saveFriend.getId();
    }

    public Friend findById(Long id) {
        return friendRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 친구가 존재하지 않습니다. ID : " + id));
    }

    public List<Friend> findAll() {
        return friendRepository.findAll();
    }

    public List<Friend> findByFriendIdAndStatus(Long friendId, FriendShip status) {
        return friendRepository.findByFriendIdAndStatus(friendId, status);
    }

    public List<Friend> findMyFriend(Long memberId) {
        return friendRepository.findMyFriend(memberId);
    }

    public Friend findByMemberIdAndFriendIdAndStatus(Long memberId, Long friendMemberId, FriendShip friendShip) {
        return friendRepository.findByMemberIdAndFriendIdAndStatus(memberId, friendMemberId, friendShip);
    }

    public FriendShip checkFriendShip(Long memberId, Long friendId) {
        return friendRepository.checkFriendShip(memberId, friendId);
    }

    public Friend fetchByIdWithMemberAndFriend(Long friendId) {
        return friendRepository.fetchByIdWithMemberAndFriend(friendId);
    }
}
