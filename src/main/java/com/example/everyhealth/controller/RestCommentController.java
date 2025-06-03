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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@Tag(name = "댓글 관리")
public class RestCommentController {

    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;


    @PostMapping("/comment")
    @Operation(summary = "댓글 저장")
    public ResponseEntity<String> save(@ExtractMemberId Long memberId,
                                     @RequestBody SaveComment dto,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors.toString());
        }
        Member member = memberService.findById(memberId);
        Post post = postService.findById(dto.getPostId());
        Comment comment = new Comment(dto.getText(), post, member);
        commentService.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/comment/{postId}")
    @Operation(summary = "포스트 ID로 댓글 조회")
    public ResponseEntity<List<CommentDto>> findByPostId(@PathVariable Long postId) {
        List<CommentDto> commentDtoList = commentService.findByPostId(postId);

        return ResponseEntity.ok(commentDtoList);
    }
}
