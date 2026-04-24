package com.example.guardrail_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.guardrail_api.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {

}
