package com.example.come_backend_story.service;

import com.example.come_backend_story.client.TSheetsClient;
import com.example.come_backend_story.entity.JobCodeEntity;
import com.example.come_backend_story.mapper.TSheetsMapper;
import com.example.come_backend_story.repository.JobCodeRepository;
import com.example.come_backend_story.response.JobCodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobCodeSyncService implements CommandLineRunner {

  private final TSheetsClient tsheetsClient;
  private final JobCodeRepository jobCodeRepository;
  private final TSheetsMapper mapper;

  /**
   * Runs automatically ONCE when the Spring Boot application starts
   */
  @Override
  public void run(String... args) {
    log.info("Starting initial full import of Job Codes from QuickBooks Workforce (TSheets)...");
    importAllJobCodes();
    log.info("Initial Job Codes import completed.");
  }

  /**
   * Full import - used only on first startup
   */
  public void importAllJobCodes() {
    int page = 1;
    boolean hasMore = true;

    while (hasMore) {
      Map<String, String> params = new HashMap<>();
      params.put("active", "both");
      params.put("limit", "200");
      params.put("page", String.valueOf(page));

      try {
        JobCodeResponse response = tsheetsClient.getJobCodes(params);

        if (response != null && response.getResults() != null
            && response.getResults().getJobcodes() != null) {

          response.getResults().getJobcodes().values().forEach(dto -> {
            JobCodeEntity entity = mapper.toJobCodeEntity(dto);
            jobCodeRepository.save(entity);
          });

          log.info("Imported {} job codes from page {}",
              response.getResults().getJobcodes().size(), page);
        }

        hasMore = Boolean.TRUE.equals(response != null ? response.getMore() : false);
        page++;
      } catch (Exception e) {
        log.error("Error importing job codes on page {}", page, e);
        break; // or implement retry logic
      }
    }

    log.info("Full Job Codes import completed successfully!");
  }

  /**
   * Incremental sync - runs every 24 hours
   * Uses modified_since to fetch only changed job codes
   */
  @Scheduled(fixedRate = 24 * 60 * 60 * 1000)   // 24 hours in milliseconds
  // @Scheduled(cron = "0 0 2 * * ?")           // Alternative: every day at 2:00 AM
  public void incrementalJobCodeSync() {
    log.info("Starting incremental Job Code sync...");

    // Get the most recent last_modified from our database
    OffsetDateTime lastSyncTime = jobCodeRepository.findMaxLastModified()
        .orElse(OffsetDateTime.now().minusDays(30)); // fallback for first incremental run

    // Format the date correctly for TSheets API (no nanoseconds)
    String modifiedSince = lastSyncTime.withNano(0).toString();   // Removes nanoseconds

    Map<String, String> params = new HashMap<>();
    params.put("modified_since", modifiedSince);  // ISO 8601 format
    params.put("active", "both");
    params.put("limit", "200");

    int page = 1;
    boolean hasMore = true;

    int updatedCount = 0;

    while (hasMore) {
      params.put("page", String.valueOf(page));

      try {
        JobCodeResponse response = tsheetsClient.getJobCodes(params);

        if (response != null && response.getResults() != null
            && response.getResults().getJobcodes() != null) {

          int count = response.getResults().getJobcodes().size();
          updatedCount += count;

          response.getResults().getJobcodes().values().forEach(dto -> {
            JobCodeEntity entity = mapper.toJobCodeEntity(dto);
            jobCodeRepository.save(entity);
          });

          log.info("Updated/created {} job codes from page {}", count, page);
        }

        hasMore = Boolean.TRUE.equals(response != null ? response.getMore() : false);
        page++;
      } catch (Exception e) {
        log.error("Error during incremental job code sync on page {}", page, e);
        break;
      }
    }

    log.info("Incremental Job Code sync completed. {} records processed.", updatedCount);
  }
}