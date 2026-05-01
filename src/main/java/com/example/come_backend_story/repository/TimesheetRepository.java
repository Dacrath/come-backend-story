package com.example.come_backend_story.repository;

import com.example.come_backend_story.entity.TimesheetEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TimesheetRepository extends JpaRepository<TimesheetEntity, Long> {

  @Query("SELECT MAX(t.lastModified) FROM TimesheetEntity t")
  Optional<OffsetDateTime> findMaxLastModified();

  List<TimesheetEntity> findByOnTheClockTrue();
  /**
   * Soft-delete timesheets by ID
   */
  @Modifying
  @Transactional
  @Query("UPDATE TimesheetEntity t SET t.deleted = true WHERE t.id IN :ids")
  int softDeleteByIds(Set<Long> ids);
}