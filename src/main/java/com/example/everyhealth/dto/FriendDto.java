package com.example.everyhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendDto {

    private Long id;
    private MemberDto member;
    private MemberDto friend;
}
