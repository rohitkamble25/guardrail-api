package com.example.guardrail_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.guardrail_api.entity.Post;

public interface PostRepo extends JpaRepository<Post, Integer> {

}
