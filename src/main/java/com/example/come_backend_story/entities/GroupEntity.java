package com.example.come_backend_story.entities;

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
  private Long id;                    // TSheets group ID

  @Column(nullable = false, length = 80)
  private String name;

  private OffsetDateTime lastModified;

  private Boolean active = true;
}