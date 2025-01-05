package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Friend {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //멤버 자신
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id") //멤버 친구
    private Member friend;

    @Enumerated(value = EnumType.STRING)
    private FriendShip status;

    public Friend(Member member) {
        this.member = member;
        member.getFriendList().add(this);
    }


    public Friend() {
    }

    public void setFriend(Member friend) {
        this.friend = friend;
    }

    public void setStatus(FriendShip status) {
        this.status = status;
    }
}
