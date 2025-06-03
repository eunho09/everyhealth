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
    private final MemberRepository memberRepository;

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


    @Cacheable(value = "friendByAccept", key = "#friendMemberId")
    public List<FriendDto> findByFriendIdAndStatus(Long friendMemberId, FriendShip status) {
        List<Friend> requestFriendList = friendRepository.findByFriendIdAndStatus(friendMemberId, status);
        return requestFriendList.stream()
                .map(f -> new FriendDto(f.getId(),
                        new MemberDto(f.getMember()),
                        new MemberDto(f.getFriend())))
                .collect(Collectors.toList());
    }



    @Cacheable(value = "friendByMember", key = "#memberId")
    public List<FriendDto> findMyFriend(Long memberId) {
        List<Friend> friendList = friendRepository.findMyFriend(memberId);

        return friendList.stream()
                .map(f -> new FriendDto(f.getId(),
                        new MemberDto(f.getMember()),
                        new MemberDto(f.getFriend())))
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = {"friendShipCheck", "friendByAccept", "friendByMember", "memberByFriend", "existsByIdAndFriendId"}, allEntries = true)
    public Long selectRequest(Long memberId, Long friendMemberId, FriendShip friendShip) {
        Friend friend = friendRepository.findByMemberIdAndFriendIdAndStatus(memberId, friendMemberId, FriendShip.REQUEST);
        friend.setStatus(friendShip);

        return friend.getId();
    }

    @Cacheable(value = "friendShipCheck", key = "#memberId")
    public boolean checkAcceptFriendShip(Long friendId, Long memberId) {
        Friend friend = friendRepository.checkAcceptFriendShip(friendId, memberId);
        return friend != null;
    }

    public FriendShip chekFriendShip(Long memberId, Long friendId) {
        return friendRepository.checkFriendShip(memberId, friendId);
    }
}
