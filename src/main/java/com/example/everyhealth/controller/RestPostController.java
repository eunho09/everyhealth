package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Post;
import com.example.everyhealth.dto.PostDto;
import com.example.everyhealth.service.FileStorageService;
import com.example.everyhealth.service.FileStore;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
@Tag(name = "포스트 관리")
public class RestPostController {

    private final MemberService memberService;
    private final PostService postService;
    private final FileStorageService fileStorageService;


    @PostMapping("/post")
    @Operation(summary = "포스트 저장")
    public ResponseEntity<Void> save(@ExtractMemberId Long memberId,
                                     @RequestPart @NotNull MultipartFile file,
                                     @RequestPart @NotBlank String text ) throws IOException {

        Member member = memberService.findById(memberId);
        String uniqueFileName = fileStorageService.uploadFile(file);
        Post post = new Post(text, uniqueFileName, member);

        postService.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/posts")
    @Operation(summary = "모든 포스트 조회")
    public ResponseEntity<List<PostDto>> findAll(@RequestParam(defaultValue = "10") int limit) {
        List<Post> postList = postService.findRecent(limit);

        List<PostDto> postDtoList = postList.stream()
                .map(post -> new PostDto(post))
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDtoList);
    }

    @GetMapping("/posts/scroll")
    @Operation(summary = "스크롤 조회")
    public ResponseEntity<List<PostDto>> scroll(@RequestParam(defaultValue = "10") int limit, @RequestParam Long postId) {
        List<Post> postList = postService.findByLtPostId(limit, postId);

        List<PostDto> postDtoList = postList.stream()
                .map(post -> new PostDto(post))
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDtoList);
    }


    @GetMapping("/member/posts")
    @Operation(summary = "나의 포스트 조회")
    public ResponseEntity<List<PostDto>> findMemberPost(@ExtractMemberId Long memberId) {
        List<PostDto> postList = postService.findByMemberId(memberId);

        return ResponseEntity.ok(postList);
    }

    @GetMapping("/images/{fileName}")
    @Operation(summary = "이미지 조회")
    public Resource viewImage(@PathVariable String fileName) throws MalformedURLException {
        return (Resource) fileStorageService.downloadFile(fileName);
    }

    @GetMapping("/{fileName}")
    @Operation(summary = "이미지 저장")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable String fileName) throws MalformedURLException {
        byte[] data = (byte[]) fileStorageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @GetMapping("/posts/friend/{friendId}")
    @Operation(summary = "친구의 포스트 조회")
    public ResponseEntity<List<PostDto>> findByFriendId(@PathVariable Long friendId) {
        List<PostDto> postDtoList = postService.findByMemberId(friendId);

        return ResponseEntity.ok(postDtoList);
    }
}
