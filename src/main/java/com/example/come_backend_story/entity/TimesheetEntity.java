package com.example.come_backend_story.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "timesheets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetEntity {

  @Id
  private Long id;                         // TSheets timesheet ID

  @Column(nullable = false)
  private Long userId;

  private Long jobcodeId;

  @Column(name = "start_time")
  private OffsetDateTime startTime;

  @Column(name = "end_time")
  private OffsetDateTime endTime;

  private Integer duration;                // in seconds (as returned by TSheets)

  @Column(name = "on_the_clock")
  private Boolean onTheClock = false;

  @Column(length = 4000)
  private String notes;

  @Column(nullable = false)
  private OffsetDateTime lastModified;

  @Column(name = "synced_at")
  private OffsetDateTime syncedAt;

  private Boolean deleted = false;         // soft delete flag
}