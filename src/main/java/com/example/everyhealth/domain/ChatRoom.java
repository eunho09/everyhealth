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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChatMessage> chatMessageList = new ArrayList<>();


    public ChatRoom(String title, Club club) {
        this.title = title;
        this.club = club;
        club.setChatRoom(this);
    }

    public ChatRoom() {
    }
}
