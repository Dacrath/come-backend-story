package com.example.come_backend_story.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class GroupDTO {
  private Long id;
  private String name;
  private Boolean active;

  @JsonProperty("last_modified")
  private OffsetDateTime lastModified;
}