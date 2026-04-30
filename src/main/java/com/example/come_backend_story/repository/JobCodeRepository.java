package com.example.come_backend_story.repository;

import com.example.come_backend_story.entities.JobCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// JobCodeRepository.java
public interface JobCodeRepository extends JpaRepository<JobCodeEntity, Long> { }