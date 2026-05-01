package com.example.come_backend_story.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserDTO {

  private Long id;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("group_id")
  private Long groupId;

  private String email;

  @JsonProperty("mobile_number")
  private String mobileNumber;

  private Boolean active;

  @JsonProperty("last_modified")
  private OffsetDateTime lastModified;
}