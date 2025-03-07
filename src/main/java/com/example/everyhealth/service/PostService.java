package com.example.everyhealth.service;

import com.example.everyhealth.domain.Post;
import com.example.everyhealth.repository.PostRepository;
import com.example.everyhealth.repository.PostSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long save(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    public Post findById(Long id) {
        return postRepository.findById(id).get();
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findByMemberId(Long memberId) {
        return postRepository.findByMemberId(memberId);
    }

    public List<Post> findRecent(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        return postRepository.findRecent(pageRequest).stream().toList();
    }

    public List<Post> findByLtPostId(int limit, Long postId) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        return postRepository.findByLtPostId(postId, pageRequest).stream().toList();
    }

    public List<Post> findByFriendId(Long friendId) {
        return postRepository.findByFriendId(friendId);
    }
}
