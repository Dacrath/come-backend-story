package com.example.come_backend_story.response;

import com.example.come_backend_story.dto.JobCodeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class JobCodeResponse {

  @JsonProperty("results")
  private Results results;

  private Boolean more;                     // true = more pages available

  @JsonProperty("supplemental_data")
  private Object supplementalData;          // can be ignored for now

  @Data
  public static class Results {
    @JsonProperty("jobcodes")
    private Map<String, JobCodeDTO> jobcodes;   // Key = id as string
  }
}
