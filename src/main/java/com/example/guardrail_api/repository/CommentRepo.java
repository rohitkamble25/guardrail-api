package com.example.guardrail_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.guardrail_api.entity.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

}
