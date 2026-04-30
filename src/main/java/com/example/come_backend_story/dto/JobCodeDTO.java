package com.example.come_backend_story.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class JobCodeDTO {

  private Long id;

  private String name;

  private Boolean active;

  private String type;                    // regular, pto, paid_break, unpaid_break, etc.

  private Boolean billable;

  @JsonProperty("assigned_to_all")
  private Boolean assignedToAll;

  @JsonProperty("parent_id")
  private Long parentId;

  @JsonProperty("short_code")
  private String shortCode;

  @JsonProperty("last_modified")
  private OffsetDateTime lastModified;

  @JsonProperty("created")
  private OffsetDateTime created;

  // You can add more fields later (customfields, locations, etc.)
}
