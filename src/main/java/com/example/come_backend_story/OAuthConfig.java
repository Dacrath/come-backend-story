package com.example.come_backend_story;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OAuthConfig {

  private final String oauthToken;

  public OAuthConfig(@Value("${oauth.token}") String oauthToken) {
    if (oauthToken == null || oauthToken.isBlank()) {
      throw new IllegalStateException("OAuth token is missing! Set the 'oauth.token' property or OAUTH_TOKEN environment variable.");
    }
    this.oauthToken = oauthToken;
    // Optional: mask it in logs
    System.out.println("OAuth token loaded successfully (length: " + oauthToken.length() + ")");
  }

  public String getOauthToken() {
    return oauthToken;
  }
}
