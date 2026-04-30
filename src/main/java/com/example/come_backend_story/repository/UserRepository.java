package com.example.come_backend_story.repository;

import com.example.come_backend_story.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  @Query("SELECT MAX(u.lastModified) FROM UserEntity u")
  Optional<OffsetDateTime> findMaxLastModified();
}