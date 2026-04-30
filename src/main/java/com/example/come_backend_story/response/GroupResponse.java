package com.example.come_backend_story.response;

import com.example.come_backend_story.dto.GroupDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class GroupResponse {

  @JsonProperty("results")
  private Results results;

  private Boolean more;

  @Data
  public static class Results {
    @JsonProperty("groups")
    private Map<String, GroupDTO> groups;
  }
}