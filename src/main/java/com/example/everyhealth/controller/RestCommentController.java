package com.example.everyhealth.controller;

import com.example.everyhealth.aop.ExtractMemberId;
import com.example.everyhealth.domain.Comment;
import com.example.everyhealth.domain.Member;
import com.example.everyhealth.domain.Post;
import com.example.everyhealth.dto.CommentDto;
import com.example.everyhealth.dto.SaveComment;
import com.example.everyhealth.security.JwtTokenGenerator;
import com.example.everyhealth.service.CommentService;
import com.example.everyhealth.service.MemberService;
import com.example.everyhealth.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class RestCommentController {

    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;


    @PostMapping("/comment")
    public ResponseEntity<Void> save(@ExtractMemberId Long memberId, @RequestBody SaveComment dto) {
        Member member = memberService.findById(memberId);
        Post post = postService.findById(dto.getPostId());
        Comment comment = new Comment(dto.getText(), post, member);
        commentService.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/comment/{postId}")
    public ResponseEntity<List<CommentDto>> findByPostId(@PathVariable Long postId) {
        List<CommentDto> commentDtoList = commentService.findByPostId(postId);

        return ResponseEntity.ok(commentDtoList);
    }
}
