package com.example.everyhealth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    TODAY_DUPLICATE_BY_DATE("400", "이미 해당 날짜의 Today가 존재합니다."),

    FRIEND_SAME_ID("400", "자신에게 친구 요청을 보낼 수 없습니다."),
    FRIEND_NULL_ID("400", "Freind의 ID가 Null입니다."),
    FRIEND_ALREADY_SEND("400", "이미 친구 요청을 보냈습니다."),
    FRIEND_ALREADY_ACCEPT("400", "이미 친구입니다."),
    FRIEND_NOT_REQUEST("400", "친구 요청이 오지 않았습니다."),
    FRIEND_NOT_ACCEPT("400", "친구 요청 상태가 아닙니다."),

    MEMBER_NOT_FRIEND("403", "친구가 아니므로 접근할 수 없습니다.");

    private final String code;
    private final String message;
}

