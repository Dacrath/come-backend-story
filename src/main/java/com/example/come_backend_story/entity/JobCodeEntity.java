package com.example.come_backend_story.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "job_code")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobCodeEntity {

  @Id
  private Long id;

  @Column(nullable = false, length = 80)
  private String name;

  private Boolean active = true;

  private String type;

  private Boolean billable;

  @Column(name = "assigned_to_all")
  private Boolean assignedToAll;

  @Column(name = "parent_id")
  private Long parentId;

  @Column(name = "short_code", length = 50)
  private String shortCode;

  @Column(name = "last_modified", nullable = false)
  private OffsetDateTime lastModified;

  @Column(name = "synced_at")
  private OffsetDateTime syncedAt;

  private Boolean deleted = false;
}
