package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Club;
import com.example.everyhealth.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.member.id=:memberId")
    List<Post> findByMemberId(@Param("memberId") Long memberId);

    @Query("select p from Post p order by p.createAt desc")
    Page<Post> findRecent(PageRequest pageRequest);

    @Query("select p from Post p where p.id >:postId order by p.createAt desc")
    Page<Post> findByLtPostId(@Param("postId") Long postId, PageRequest pageRequest);

    @Query("select p from Post p where p.member.id=(select f.friend.id from Friend f where f.id=:friendId)")
    List<Post> findByFriendId(@Param("friendId") Long friendId);
}
