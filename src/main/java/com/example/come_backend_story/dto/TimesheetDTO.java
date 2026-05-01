package com.example.come_backend_story.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TimesheetDTO {
  private Long id;

  @JsonProperty("user_id")
  private Long userId;

  @JsonProperty("jobcode_id")
  private Long jobcodeId;

  @JsonProperty("start")
  private OffsetDateTime startTime;

  @JsonProperty("end")
  private OffsetDateTime endTime;

  private Integer duration;

  @JsonProperty("on_the_clock")
  private Boolean onTheClock;

  private String notes;

  @JsonProperty("last_modified")
  private OffsetDateTime lastModified;

  @JsonProperty("tz")           // ← Add this
  private Integer tz;

  @JsonProperty("tz_str")       // ← Add this
  private String tzStr;
}