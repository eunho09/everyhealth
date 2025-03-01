package com.example.everyhealth.repository;

import com.example.everyhealth.domain.Club;
import com.example.everyhealth.domain.ClubMember;
import com.example.everyhealth.domain.Member;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ClubSpecification {

    public static Specification<Club> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()){
                return cb.conjunction();
            }

            return cb.like(root.get("title"), "%" + name + "%");
        };
    }

    public static Specification<Club> joinMember(Long memberId) {
        return (root, query, cb) -> {
            if (memberId == null || memberId == 0){
                return cb.conjunction();
            }

            Join<Club, ClubMember> clubMemberJoin = root.join("clubMemberList");
            return cb.equal(clubMemberJoin.get("member").get("id"), memberId);
        };
    }
}
