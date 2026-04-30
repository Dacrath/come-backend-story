package com.example.come_backend_story.repository;

import com.example.come_backend_story.entity.JobCodeEntity;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// JobCodeRepository.java
public interface JobCodeRepository extends JpaRepository<JobCodeEntity, Long> {
  @Query("SELECT MAX(j.lastModified) FROM JobCodeEntity j")
  Optional<OffsetDateTime> findMaxLastModified();

}