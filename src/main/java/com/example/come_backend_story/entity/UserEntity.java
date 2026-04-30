package com.example.come_backend_story.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

  @Id
  private Long id;

  @Column(name = "first_name", length = 80)
  private String firstName;

  @Column(name = "last_name", length = 80)
  private String lastName;

  @Column(name = "group_id")
  private Long groupId;

  @Column(length = 100)
  private String email;

  @Column(name = "cell_number", length = 15)
  private String cellNumber;

  private Boolean active = true;

  @Column(name = "last_modified")
  private OffsetDateTime lastModified;

  @Column(name = "synced_at")
  private OffsetDateTime syncedAt;

  private Boolean deleted = false;
}