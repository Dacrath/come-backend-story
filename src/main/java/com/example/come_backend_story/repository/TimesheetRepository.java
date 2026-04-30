package com.example.come_backend_story.repository;

import com.example.come_backend_story.entity.TimesheetEntity;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// TimesheetRepository.java
public interface TimesheetRepository extends JpaRepository<TimesheetEntity, Long> {

  // Useful query methods you may need later
  List<TimesheetEntity> findByLastModifiedAfter(OffsetDateTime lastModified);

  List<TimesheetEntity> findByOnTheClockTrue();
}