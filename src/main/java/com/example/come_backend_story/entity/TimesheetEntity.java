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
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "jobcode_id")
  private Long jobcodeId;

  @Column(name = "start_time")
  private OffsetDateTime startTime;     // Keep as OffsetDateTime (best practice)

  @Column(name = "end_time")
  private OffsetDateTime endTime;       // Keep as OffsetDateTime

  private Integer duration;

  @Column(name = "on_the_clock")
  private Boolean onTheClock = false;

  @Column(length = 4000)
  private String notes;

  @Column(name = "last_modified", nullable = false)
  private OffsetDateTime lastModified;

  @Column(name = "synced_at")
  private OffsetDateTime syncedAt;

  private Boolean deleted = false;

  // New timezone columns
  private Integer tz;                   // e.g. -7, -6, -5
  @Column(name = "tz_str", length = 20)
  private String tzStr;                 // e.g. "tsPT", "tsMT", etc.
}