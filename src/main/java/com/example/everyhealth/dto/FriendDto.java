package com.example.everyhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto {

    private Long id;
    private MemberDto member;
    private MemberDto friend;
}
