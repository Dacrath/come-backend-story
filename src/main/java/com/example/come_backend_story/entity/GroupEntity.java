package com.example.come_backend_story.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupEntity {

  @Id
  private Long id;

  @Column(nullable = false, length = 80)
  private String name;

  private Boolean active = true;

  @Column(name = "last_modified")
  private OffsetDateTime lastModified;

  @Column(name = "synced_at")
  private OffsetDateTime syncedAt;

  private Boolean deleted = false;
}