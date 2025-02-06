package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoom {

    @Id @GeneratedValue
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessageList = new ArrayList<>();


    public ChatRoom(String title, Member member) {
        this.title = title;
        this.member = member;
        member.getChatRoomList().add(this);
    }

    public ChatRoom() {
    }
}
