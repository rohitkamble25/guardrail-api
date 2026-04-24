package com.example.guardrail_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.guardrail_api.entity.InteractionType;
import com.example.guardrail_api.entity.Post;
import com.example.guardrail_api.repository.PostRepo;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private RedisService redisService;

    public Post createPost(Post post) {
        return postRepo.save(post);
    }

    public void updateVirality(int postId, InteractionType type) {

        String key = "post:" + postId + ":virality_score";

        switch (type) {
            case BOT_REPLY:
                redisService.incrementBy(key, 1);
                break;
            case HUMAN_LIKE:
                redisService.incrementBy(key, 20);
                break;
            case HUMAN_COMMENT:
                redisService.incrementBy(key, 50);
                break;
        }
    }
}