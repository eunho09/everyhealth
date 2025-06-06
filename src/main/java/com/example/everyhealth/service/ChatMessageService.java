package com.example.everyhealth.service;


import com.example.everyhealth.domain.ChatMessage;
import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.ChatMessageResponseDto;
import com.example.everyhealth.dto.ChatMessageSaveDto;
import com.example.everyhealth.dto.MemberChatResponseDto;
import com.example.everyhealth.exception.EmptyDataException;
import com.example.everyhealth.exception.ErrorCode;
import com.example.everyhealth.repository.ChatMessageRepository;
import com.example.everyhealth.repository.ChatRoomRepository;
import com.example.everyhealth.repository.ClubMemberRepository;
import com.example.everyhealth.repository.MemberRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public ChatMessageSaveDto saveMessage(String message, Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new EntityNotFoundException("채팅방이 존재하지 않습니다. ID : " + chatRoomId));
        ClubMember clubMember = clubMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId);

        if (clubMember == null) {
            throw new AccessDeniedException("채팅방에 참여하지 않은 사용자입니다. 채팅방 ID : " + chatRoomId + ", 회원 ID : " + memberId);
        }

        if (StringUtils.isEmpty(message)) {
            throw new EmptyDataException(ErrorCode.EMPTY_MESSAGE_DATA, message);
        }

        String cleanMessage = message.replace("\"", "");

        ChatMessage chatMessage = new ChatMessage(cleanMessage, clubMember, chatRoom);
        ChatMessage saveMessage = chatMessageRepository.save(chatMessage);

        MemberChatResponseDto memberChatResponseDto = new MemberChatResponseDto(clubMember);

        return new ChatMessageSaveDto(cleanMessage, saveMessage.getId(), memberChatResponseDto, chatMessage.getCreatedDate());
    }

    public List<ChatMessage> findByRecentMessage(Long roomId, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        Page<ChatMessage> messages = chatMessageRepository.findByRecentMessage(roomId, pageRequest);

        return messages.stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedDate))
                .toList();
    }

    public List<ChatMessage> findOlderMessages(Long roomId, Long messageId, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        Page<ChatMessage> messages = chatMessageRepository.findOlderMessages(roomId, messageId, pageRequest);

        return messages.stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedDate))
                .toList();
    }
}
