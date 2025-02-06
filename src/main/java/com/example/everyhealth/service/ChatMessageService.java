package com.example.everyhealth.service;


import com.example.everyhealth.domain.ChatMessage;
import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.dto.ChatMessageResponseDto;
import com.example.everyhealth.repository.ChatMessageRepository;
import com.example.everyhealth.repository.ChatRoomRepository;
import com.example.everyhealth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatMessageResponseDto chatMessageResponse(String message, Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).get();
        Member member = memberRepository.findById(memberId).get();

        ChatMessage chatMessage = new ChatMessage(message, member, chatRoom);

        chatMessageRepository.save(chatMessage);

        return new ChatMessageResponseDto(message, member);
    }
}
