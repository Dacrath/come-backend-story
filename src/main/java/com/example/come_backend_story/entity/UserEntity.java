package com.example.come_backend_story.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "users")          // renamed from "user" (reserved word in many DBs)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

  @Id
  private Long id;

  @Column(length = 80)
  private String firstName;

  @Column(length = 80)
  private String lastName;

  private Long groupId;

  @Column(length = 100)
  private String email;

  @Column(length = 15)
  private String cellNumber;

  private OffsetDateTime lastModified;

  private Boolean active = true;
}