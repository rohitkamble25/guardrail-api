package com.example.guardrail_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.guardrail_api.entity.Comment;
import com.example.guardrail_api.service.CommentService;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}/comments")
    public Comment addComment(@PathVariable int postId, @RequestBody Comment comment) {
        return commentService.addComment(postId, comment);
    }
}