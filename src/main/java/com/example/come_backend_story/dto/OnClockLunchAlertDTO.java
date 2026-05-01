package com.example.come_backend_story.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class OnClockLunchAlertDTO {

  private Long userId;
  private String fullName;
  private String groupName;           // ← Now returns the actual name

  private Double currentPaidHours;
  private Integer lunchNumberNeeded;
  private Integer minutesUntilDeadline;

  private String deadlineTime;
  private OffsetDateTime clockInTime;
  private Integer tz;
  private String tzStr;

  private Boolean hasTakenLunchToday;
}