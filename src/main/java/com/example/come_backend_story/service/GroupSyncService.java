package com.example.come_backend_story.service;

import com.example.come_backend_story.client.TSheetsClient;
import com.example.come_backend_story.entity.GroupEntity;
import com.example.come_backend_story.mapper.TSheetsMapper;
import com.example.come_backend_story.repository.GroupRepository;
import com.example.come_backend_story.response.GroupResponse;
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
public class GroupSyncService implements CommandLineRunner {

  private final TSheetsClient tsheetsClient;
  private final GroupRepository groupRepository;
  private final TSheetsMapper mapper;

  @Override
  public void run(String... args) {
    log.info("Starting initial full import of Groups from TSheets...");
    importAllGroups();
    log.info("Initial Groups import completed.");
  }

  public void importAllGroups() {
    int page = 1;
    boolean hasMore = true;

    while (hasMore) {
      Map<String, String> params = new HashMap<>();
      params.put("active", "both");
      params.put("limit", "200");
      params.put("page", String.valueOf(page));

      try {
        GroupResponse response = tsheetsClient.getGroups(params);

        if (response != null && response.getResults() != null
            && response.getResults().getGroups() != null) {

          response.getResults().getGroups().values().forEach(dto -> {
            GroupEntity entity = mapper.toGroupEntity(dto);
            groupRepository.save(entity);
          });

          log.info("Imported {} groups from page {}",
              response.getResults().getGroups().size(), page);
        }

        hasMore = Boolean.TRUE.equals(response != null ? response.getMore() : false);
        page++;
      } catch (Exception e) {
        log.error("Error importing groups on page {}", page, e);
        break;
      }
    }

    log.info("Full Groups import completed successfully!");
  }

  @Scheduled(fixedRate = 60 * 60 * 1000)  // Every 1 hour (3600000 ms)
  public void incrementalGroupSync() {
    log.info("Starting incremental Group sync...");

    OffsetDateTime lastSyncTime = groupRepository.findMaxLastModified()
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
        GroupResponse response = tsheetsClient.getGroups(params);

        if (response != null && response.getResults() != null
            && response.getResults().getGroups() != null) {

          int count = response.getResults().getGroups().size();
          updatedCount += count;

          response.getResults().getGroups().values().forEach(dto -> {
            GroupEntity entity = mapper.toGroupEntity(dto);
            groupRepository.save(entity);
          });

          log.info("Updated/created {} groups from page {}", count, page);
        }

        hasMore = Boolean.TRUE.equals(response != null ? response.getMore() : false);
        page++;
      } catch (Exception e) {
        log.error("Error during incremental group sync on page {}", page, e);
        break;
      }
    }

    log.info("Incremental Group sync completed. {} records processed.", updatedCount);
  }
}