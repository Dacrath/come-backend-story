package com.example.come_backend_story.client;

import com.example.come_backend_story.response.GroupResponse;
import com.example.come_backend_story.response.JobCodeResponse;
import com.example.come_backend_story.response.TimesheetDeletedResponse;
import com.example.come_backend_story.response.TimesheetResponse;
import com.example.come_backend_story.response.UserResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class TSheetsClient {

  private final RestTemplate restTemplate;
  private final String baseUrl = "https://rest.tsheets.com/api/v1";

  public TSheetsClient(@Value("${oauth.token}") String oauthToken, RestTemplate restTemplate) {
    this.restTemplate = restTemplate;

    // Add Authorization header to all requests
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + oauthToken);

    this.restTemplate.setInterceptors(List.of((request, body, execution) -> {
      request.getHeaders().addAll(headers);
      return execution.execute(request, body);
    }));
  }

  // ==================== Job Codes ====================
  public JobCodeResponse getJobCodes(Map<String, String> params) {
    String url = buildUrl("/jobcodes", params);
    return restTemplate.getForObject(url, JobCodeResponse.class);
  }

  // ==================== Groups ====================
  public GroupResponse getGroups(Map<String, String> params) {
    String url = buildUrl("/groups", params);
    return restTemplate.getForObject(url, GroupResponse.class);
  }

  // ==================== Users ====================
  public UserResponse getUsers(Map<String, String> params) {
    String url = buildUrl("/users", params);
    return restTemplate.getForObject(url, UserResponse.class);
  }

  // ==================== Timesheets ====================
  public TimesheetResponse getTimesheets(Map<String, String> params) {
    String url = buildUrl("/timesheets", params);
    return restTemplate.getForObject(url, TimesheetResponse.class);
  }

  public TimesheetDeletedResponse getDeletedTimesheets(Map<String, String> params) {
    String url = buildUrl("/timesheets_deleted", params);
    return restTemplate.getForObject(url, TimesheetDeletedResponse.class);
  }

  // Helper method to build URL with query parameters
  private String buildUrl(String endpoint, Map<String, String> params) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl + endpoint);

    if (params != null && !params.isEmpty()) {
      params.forEach(builder::queryParam);
    }

    return builder.toUriString();
  }
}