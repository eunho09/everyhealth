package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Post;
import com.example.everyhealth.domain.UploadFile;
import com.example.everyhealth.dto.PostDto;
import com.example.everyhealth.dto.UploadPostDto;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.FileStore;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class RestPostController {

    private final MemberService memberService;
    private final PostService postService;
    private final FileStore fileStore;


    @PostMapping("/post")
    public ResponseEntity<Void> save(@ExtractMemberId Long memberId,
                                     @RequestPart MultipartFile file,
                                     @RequestPart String text ) throws IOException {

        Member member = memberService.findById(memberId);
        UploadFile uploadFile = fileStore.storeFile(file);
        String storeFileName = uploadFile.getStoreFileName();
        Post post = new Post(text, storeFileName, member);

        postService.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> findAll(@RequestParam(defaultValue = "10") int limit) {
        List<Post> postList = postService.findRecent(limit);

        List<PostDto> postDtoList = postList.stream()
                .map(post -> new PostDto(post.getId(), post.getText(), post.getImageUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDtoList);
    }

    @GetMapping("/posts/scroll")
    public ResponseEntity<List<PostDto>> scroll(@RequestParam(defaultValue = "10") int limit, @RequestParam Long postId) {
        List<Post> postList = postService.findByLtPostId(limit, postId);

        List<PostDto> postDtoList = postList.stream()
                .map(post -> new PostDto(post.getId(), post.getText(), post.getImageUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDtoList);
    }


    @GetMapping("/member/posts")
    public ResponseEntity<List<PostDto>> findMemberPost(@ExtractMemberId Long memberId) {
        List<Post> postList = postService.findByMemberId(memberId);

        List<PostDto> postDtoList = postList.stream()
                .map(post -> new PostDto(post.getId(), post.getText(), post.getImageUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDtoList);
    }

    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullName(fileName));
    }

    @GetMapping("/posts/friend/{friendId}")
    public ResponseEntity<List<PostDto>> findByFriendId(@PathVariable Long friendId) {
        List<Post> postList = postService.findByFriendId(friendId);

        List<PostDto> postDtoList = postList.stream()
                .map(post -> new PostDto(post.getId(), post.getText(), post.getImageUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDtoList);
    }
}
