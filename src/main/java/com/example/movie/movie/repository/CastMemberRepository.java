package com.example.movie.movie.repository;

import com.example.movie.movie.entity.CastMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CastMemberRepository extends JpaRepository<CastMember, Long> {
}
