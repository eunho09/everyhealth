package com.example.everyhealth.service;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Post;
import com.example.everyhealth.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostBusinessService {

    private final MemberService memberService;
    private final FileStorageService fileStorageService;
    private final PostDataService postDataService;

    @Transactional
    public Long createPost(Long memberId, MultipartFile file, String text) {
        Member member = memberService.findById(memberId);
        String uniqueFileName = fileStorageService.uploadFile(file);
        Post post = new Post(text, uniqueFileName, member);

        postDataService.save(post);
        return post.getId();
    }

    public List<PostDto> getRecentPosts(int limit) {
        List<Post> postList = postDataService.findRecent(limit);

        return postList.stream()
                .map(post -> new PostDto(post))
                .collect(Collectors.toList());
    }

    public List<PostDto> getScrollPosts(Long postId, int limit) {
        List<Post> postList = postDataService.findByLtPostId(limit, postId);

        return postList.stream()
                .map(post -> new PostDto(post))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "postByMember", key = "#memberId")
    public List<PostDto> findByMemberId(Long memberId) {
        List<Post> postList = postDataService.findByMemberId(memberId);

        return postList.stream()
                .map(post -> new PostDto(post))
                .collect(Collectors.toList());
    }
}
