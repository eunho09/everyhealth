package com.example.everyhealth.service;

import com.example.everyhealth.domain.Comment;
import com.example.everyhealth.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
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

    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }
}
