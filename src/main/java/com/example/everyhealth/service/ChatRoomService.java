package com.example.everyhealth.service;

import com.example.everyhealth.domain.ChatRoom;
import com.example.everyhealth.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public Long save(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
    }

    public ChatRoom findById(Long id) {
        return chatRoomRepository.findById(id).get();
    }

    public List<ChatRoom> findAll() {
        return chatRoomRepository.findAll();
    }

/*    public Long create(String title, Member member) {
        ChatRoom chatRoom = new ChatRoom(title, member);
        return save(chatRoom);
    }*/
}
