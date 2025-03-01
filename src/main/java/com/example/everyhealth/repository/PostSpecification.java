package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Post;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {

    public static Specification<Post> lastPostId(Long postId) {
        return (root, query, cb) -> {
            if (postId == null) {
                return cb.conjunction();
            }

            return cb.lt(root.get("id"), postId);
        };
    }
}
