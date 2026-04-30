package com.example.come_backend_story.service;

import com.example.come_backend_story.client.TSheetsClient;
import com.example.come_backend_story.entity.UserEntity;
import com.example.come_backend_story.mapper.TSheetsMapper;
import com.example.come_backend_story.repository.UserRepository;
import com.example.come_backend_story.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSyncService implements CommandLineRunner {

  private final TSheetsClient tsheetsClient;
  private final UserRepository userRepository;
  private final TSheetsMapper mapper;

  @Override
  public void run(String... args) {
    log.info("Starting initial full import of Users from TSheets...");
    importAllUsers();
    log.info("Initial Users import completed.");
  }

  public void importAllUsers() {
    int page = 1;
    boolean hasMore = true;

    while (hasMore) {
      Map<String, String> params = new HashMap<>();
      params.put("active", "both");
      params.put("limit", "200");
      params.put("page", String.valueOf(page));

      try {
        UserResponse response = tsheetsClient.getUsers(params);

        if (response != null && response.getResults() != null
            && response.getResults().getUsers() != null) {

          response.getResults().getUsers().values().forEach(dto -> {
            UserEntity entity = mapper.toUserEntity(dto);
            userRepository.save(entity);
          });

          log.info("Imported {} users from page {}",
              response.getResults().getUsers().size(), page);
        }

        hasMore = Boolean.TRUE.equals(response != null ? response.getMore() : false);
        page++;
      } catch (Exception e) {
        log.error("Error importing users on page {}", page, e);
        break;
      }
    }

    log.info("Full Users import completed successfully!");
  }

  @Scheduled(fixedRate = 60 * 60 * 1000)  // Every 1 hour
  public void incrementalUserSync() {
    log.info("Starting incremental User sync...");

    OffsetDateTime lastSyncTime = userRepository.findMaxLastModified()
        .orElse(OffsetDateTime.now().minusDays(30));

    String modifiedSince = lastSyncTime.withNano(0)
        .withOffsetSameInstant(java.time.ZoneOffset.UTC)
        .toString();

    Map<String, String> params = new HashMap<>();
    params.put("modified_since", modifiedSince);
    params.put("active", "both");
    params.put("limit", "200");

    int page = 1;
    boolean hasMore = true;
    int updatedCount = 0;

    while (hasMore) {
      params.put("page", String.valueOf(page));

      try {
        UserResponse response = tsheetsClient.getUsers(params);

        if (response != null && response.getResults() != null
            && response.getResults().getUsers() != null) {

          int count = response.getResults().getUsers().size();
          updatedCount += count;

          response.getResults().getUsers().values().forEach(dto -> {
            UserEntity entity = mapper.toUserEntity(dto);
            userRepository.save(entity);
          });

          log.info("Updated/created {} users from page {}", count, page);
        }

        hasMore = Boolean.TRUE.equals(response != null ? response.getMore() : false);
        page++;
      } catch (Exception e) {
        log.error("Error during incremental user sync on page {}", page, e);
        break;
      }
    }

    log.info("Incremental User sync completed. {} records processed.", updatedCount);
  }
}