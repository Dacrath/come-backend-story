package com.example.come_backend_story.repository;

import com.example.come_backend_story.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// UserRepository.java
public interface UserRepository extends JpaRepository<UserEntity, Long> { }




