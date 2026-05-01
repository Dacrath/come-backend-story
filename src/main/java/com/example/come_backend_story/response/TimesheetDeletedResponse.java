package com.example.come_backend_story.response;

import com.example.come_backend_story.dto.TimesheetDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class TimesheetDeletedResponse {

  @JsonProperty("results")
  private Results results;

  private Boolean more;

  @Data
  public static class Results {
    @JsonProperty("timesheets_deleted")
    private Map<String, TimesheetDTO> timesheetsDeleted;
  }
}