package com.example.come_backend_story.entities;

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

  private OffsetDateTime lastModified;

  private Boolean active = true;
}