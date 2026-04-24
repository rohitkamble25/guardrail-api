package com.example.guardrail_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.guardrail_api.entity.InteractionType;
import com.example.guardrail_api.entity.Post;
import com.example.guardrail_api.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PostMapping("/{postId}/like")
    public String likePost(@PathVariable int postId) {
        postService.updateVirality(postId, InteractionType.HUMAN_LIKE);
        return "Post " + postId + " liked!";
    }
}