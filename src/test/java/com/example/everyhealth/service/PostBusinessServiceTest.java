package com.example.everyhealth.service;

import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.MemberRole;
import com.example.everyhealth.domain.Post;
import com.example.everyhealth.dto.PostDto;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestPropertySource(locations = "file:C:/coding/spring/project/everyhealth/src/main/resources/.env-local")
class PostBusinessServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    PostBusinessService postBusinessService;
    @Autowired
    PostDataService postDataService;
    @Autowired
    EntityManager em;

    private Long memberId;
    private Long memberId2;

    @BeforeEach
    void init() {
        // 멤버 생성 및 저장
        Member member = new Member("길동", "test1", MemberRole.USER, "test1", "test1");
        memberId = memberService.save(member);

        Member member2 = new Member("홍길", "test2", MemberRole.USER, "test2", "test2");
        memberId2 = memberService.save(member2);
    }

    @Test
    @DisplayName("포스트 업로드")
    void create() {
        Long postId = postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi");
        Post post = postDataService.findById(postId);

        Assertions.assertThat(post.getText()).isEqualTo("hi");
    }

    @Test
    @DisplayName("최근 포스트 조회")
    void getRecentPost() {
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi1");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi2");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi3");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi4");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi5");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi6");

        em.clear();
        List<PostDto> recentPosts = postBusinessService.getRecentPosts(5);

        Assertions.assertThat(recentPosts)
                .hasSize(5)
                .extracting(p -> p.getText())
                .containsExactly("hi6", "hi5", "hi4", "hi3", "hi2");

    }

    @Test
    @DisplayName("스크롤 조회")
    void scroll() {
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi1");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi2");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi3");
        Long postId = postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi4");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi5");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi6");

        em.clear();
        List<PostDto> scrollPosts = postBusinessService.getScrollPosts(postId, 2);

        Assertions.assertThat(scrollPosts)
                .hasSize(2)
                .extracting(p -> p.getText())
                .containsExactly("hi3", "hi2");
    }

    @Test
    @DisplayName("나의 포스트 조회")
    void memberPosts() {
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi1");
        postBusinessService.createPost(memberId2, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi2");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi3");
        postBusinessService.createPost(memberId2, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi4");
        postBusinessService.createPost(memberId, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi5");
        postBusinessService.createPost(memberId2, new MockMultipartFile("unknown", "unknown.jpg", "image/jpeg", "test".getBytes()), "hi6");

        em.clear();
        List<PostDto> myPosts = postBusinessService.findByMemberId(memberId);
        Assertions.assertThat(myPosts)
                .hasSize(3)
                .extracting(p -> p.getText())
                .containsExactly("hi1", "hi3", "hi5");


    }
}