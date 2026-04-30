package com.example.come_backend_story.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserDTO {
  private Long id;
  private String firstName;
  private String lastName;
  private Long groupId;
  private String email;
  private String cellNumber;
  private Boolean active;

  @JsonProperty("last_modified")
  private OffsetDateTime lastModified;
}