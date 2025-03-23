package com.example.everyhealth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ClubMember {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean isAdmin;

    private LocalDateTime joinAt;

    @OneToMany(mappedBy = "clubMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    protected ClubMember() {
    }

    public ClubMember(Club club, Member member,boolean isAdmin, LocalDateTime joinAt) {
        setClub(club);
        setMember(member);
        this.isAdmin = isAdmin;
        this.joinAt = joinAt;
    }

    public void setClub(Club club) {
        this.club = club;
        club.getClubMemberList().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getClubMemberList().add(this);
    }
}
