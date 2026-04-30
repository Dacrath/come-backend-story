package com.example.come_backend_story.client;

import com.example.come_backend_story.response.JobCodeResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class TSheetsClient {

  private final RestTemplate restTemplate;
  private final String baseUrl = "https://rest.tsheets.com/api/v1";

  public TSheetsClient(@Value("${oauth.token}") String oauthToken, RestTemplate restTemplate) {
    this.restTemplate = restTemplate;

    // Set default headers with Bearer token
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + oauthToken);
    this.restTemplate.setInterceptors(List.of(
        (request, body, execution) -> {
          request.getHeaders().addAll(headers);
          return execution.execute(request, body);
        }
    ));
  }

  /**
   * Fetch Job Codes - using RestTemplate
   */
  public JobCodeResponse getJobCodes(Map<String, String> params) {
    String url = baseUrl + "/jobcodes";

    // Build query parameters
    StringBuilder query = new StringBuilder();
    params.forEach((key, value) -> {
      if (query.length() > 0) query.append("&");
      query.append(key).append("=").append(value);
    });

    if (query.length() > 0) {
      url += "?" + query;
    }

    return restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        JobCodeResponse.class
    ).getBody();
  }
}