package com.example.guardrail_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.example.guardrail_api.entity.*;
import com.example.guardrail_api.repository.CommentRepo;
import com.example.guardrail_api.repository.PostRepo;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PostService postService;

    public Comment addComment(int postId, Comment comment) {

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (comment.getDepthLevel() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Depth limit exceeded");
        }

        if (comment.getAuthorType() == AuthorType.BOT) {

            String botKey = "post:" + postId + ":bot_count";
            Long count = redisService.increment(botKey);

            if (count > 100) {
                redisService.incrementBy(botKey, -1);
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Bot limit exceeded");
            }

            if (post.getAuthorType() == AuthorType.USER) {

                String cooldownKey = "cooldown:bot_" + comment.getAuthorId()
                        + ":human_" + post.getAuthorId();

                if (redisService.exists(cooldownKey)) {
                    redisService.incrementBy(botKey, -1);
                    throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Cooldown active");
                }

                redisService.setWithTTL(cooldownKey, "1", 10);

                String notifKey = "user:" + post.getAuthorId() + ":pending_notifs";
                String notifCooldownKey = "notif_cooldown:user_" + post.getAuthorId();

                String message = "Bot " + comment.getAuthorId() + " replied to your post";

                if (redisService.exists(notifCooldownKey)) {
                    redisService.pushToList(notifKey, message);
                } else {
                    System.out.println("Push Notification Sent to User");
                    redisService.setWithTTL(notifCooldownKey, "1", 15);
                }
            }

            postService.updateVirality(postId, InteractionType.BOT_REPLY);

        } else {
            postService.updateVirality(postId, InteractionType.HUMAN_COMMENT);
        }

        comment.setPost(post);

        return commentRepo.save(comment);
    }
}