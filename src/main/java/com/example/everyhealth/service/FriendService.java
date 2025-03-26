package com.example.everyhealth.service;

import com.example.everyhealth.domain.Friend;
import com.example.everyhealth.domain.FriendShip;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.FriendDto;
import com.example.everyhealth.dto.MemberDto;
import com.example.everyhealth.repository.FriendRepository;
import com.example.everyhealth.repository.MemberRepository;
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
public class FriendService {

    private final FriendRepository friendRepository;

    @Transactional
    @CacheEvict(value = {"friendShipCheck", "friendByAccept", "friendByMember"}, allEntries = true)
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


    @Cacheable(value = "friendByAccept", key = "#friendMemberId")
    public List<FriendDto> findByFriendIdAndStatus(Long friendMemberId, FriendShip status) {
        List<Friend> requestFriendList = friendRepository.findByFriendIdAndStatus(friendMemberId, status);
        return requestFriendList.stream()
                .map(f -> new FriendDto(f.getId(),
                        new MemberDto(f.getMember().getId(),
                                f.getMember().getName(),
                                f.getMember().getPicture()),
                        new MemberDto(f.getFriend().getId(),
                                f.getFriend().getName(),
                                f.getFriend().getPicture())))
                .collect(Collectors.toList());
    }



    @Cacheable(value = "friendByMember", key = "#memberId")
    public List<FriendDto> findMyFriend(Long memberId) {
        List<Friend> friendList = friendRepository.findMyFriend(memberId);

        return friendList.stream()
                .map(f -> new FriendDto(f.getId(),
                        new MemberDto(f.getMember().getId(),
                                f.getMember().getName(),
                                f.getMember().getPicture()),
                        new MemberDto(f.getFriend().getId(),
                                f.getFriend().getName(),
                                f.getFriend().getPicture())))
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = {"friendShipCheck", "friendByAccept", "friendByMember", "memberByFriend", "existsByIdAndFriendId"}, allEntries = true)
    public void selectRequest(Long memberId, Long friendMemberId, FriendShip friendShip) {
        Friend friend = friendRepository.findByMemberIdAndFriendIdAndStatus(memberId, friendMemberId, FriendShip.REQUEST);
        friend.setStatus(friendShip);
    }

    @Cacheable(value = "friendShipCheck", key = "#memberId")
    public boolean checkFriendShip(Long friendId, Long memberId) {
        Friend friend = friendRepository.checkFriendShip(friendId, memberId);
        return friend != null;
    }
}
