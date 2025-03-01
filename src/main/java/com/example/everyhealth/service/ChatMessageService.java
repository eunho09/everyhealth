package com.example.everyhealth.service;


import com.example.everyhealth.domain.ChatMessage;
import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.ChatMessageResponseDto;
import com.example.everyhealth.dto.MemberChatResponseDto;
import com.example.everyhealth.repository.ChatMessageRepository;
import com.example.everyhealth.repository.ChatRoomRepository;
import com.example.everyhealth.repository.ClubMemberRepository;
import com.example.everyhealth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ClubMemberRepository clubMemberRepository;

    @Transactional
    public ChatMessageResponseDto saveMessage(String message, Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).get();
        ClubMember clubMember = clubMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId);

        String cleanMessage = message.replace("\"", "");

        ChatMessage chatMessage = new ChatMessage(cleanMessage, clubMember, chatRoom);
        ChatMessage saveMessage = chatMessageRepository.save(chatMessage);

        MemberChatResponseDto memberChatResponseDto = new MemberChatResponseDto(clubMember.getMember().getId(), clubMember.getMember().getName(), clubMember.getMember().getPicture());

        return new ChatMessageResponseDto(cleanMessage, saveMessage.getId(), memberChatResponseDto, chatMessage.getCreatedDate());
    }

    public List<ChatMessage> findByRecentMessage(Long roomId, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        Page<ChatMessage> messages = chatMessageRepository.findByRecentMessage(roomId, pageRequest);

        return messages.stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedDate))
                .toList();
    }

    public List<ChatMessage> findOlderMessages(Long roomId, Long messageId, int limit) {
        log.info("findOlderMessages");
        PageRequest pageRequest = PageRequest.of(0, limit);
        Page<ChatMessage> messages = chatMessageRepository.findOlderMessages(roomId, messageId, pageRequest);

        return messages.stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedDate))
                .toList();
    }
}
