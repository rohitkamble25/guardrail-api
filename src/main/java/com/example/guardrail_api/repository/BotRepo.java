package com.example.guardrail_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.guardrail_api.entity.Bot;

public interface BotRepo extends JpaRepository<Bot, Integer> {

}
