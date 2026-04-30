package com.example.come_backend_story.repository;

import com.example.come_backend_story.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

  @Query("SELECT MAX(g.lastModified) FROM GroupEntity g")
  Optional<OffsetDateTime> findMaxLastModified();
}