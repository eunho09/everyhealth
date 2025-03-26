package com.example.everyhealth.service;

import com.example.everyhealth.domain.Comment;
import com.example.everyhealth.dto.CommentDto;
import com.example.everyhealth.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    @CachePut(value = "comments", key = "#comment.id")
    @CacheEvict(value = "commentsAll", allEntries = true)
    public Long save(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).get();
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Cacheable(value = "commentsAll", key = "#postId")
    public List<CommentDto> findByPostId(Long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);

        return commentList.stream()
                .map(comment -> new CommentDto(comment.getId(), comment.getText(), comment.getMember().getName(), comment.getLocalDateTime()))
                .collect(Collectors.toList());
    }
}
