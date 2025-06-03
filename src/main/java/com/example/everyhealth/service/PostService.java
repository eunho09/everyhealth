package com.example.everyhealth.service;

import com.example.everyhealth.domain.Post;
import com.example.everyhealth.dto.PostDto;
import com.example.everyhealth.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    @CacheEvict(value = {"postByMember", "postByFriend"}, key = "#post.id")
    public Long save(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 포스터가 존재하지 않습니다. ID : " + id));
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Cacheable(value = "postByMember", key = "#memberId")
    public List<PostDto> findByMemberId(Long memberId) {
        List<Post> postList = postRepository.findByMemberId(memberId);

        return postList.stream()
                .map(post -> new PostDto(post))
                .collect(Collectors.toList());
    }

    public List<Post> findRecent(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        return postRepository.findRecent(pageRequest).stream().toList();
    }

    public List<Post> findByLtPostId(int limit, Long postId) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        return postRepository.findByLtPostId(postId, pageRequest).stream().toList();
    }
}
