package com.example.come_backend_story.service;

import com.example.come_backend_story.client.TSheetsClient;
import com.example.come_backend_story.dto.TimesheetDTO;
import com.example.come_backend_story.entity.TimesheetEntity;
import com.example.come_backend_story.mapper.TSheetsMapper;
import com.example.come_backend_story.repository.TimesheetRepository;
import com.example.come_backend_story.response.TimesheetDeletedResponse;
import com.example.come_backend_story.response.TimesheetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Order(10)
public class TimesheetSyncService implements CommandLineRunner {

  private final TSheetsClient tsheetsClient;
  private final TimesheetRepository timesheetRepository;
  private final UserSyncService userSyncService;      // Inject these
  private final JobCodeSyncService jobCodeSyncService;
  private final TSheetsMapper mapper;
  private final GroupSyncService groupSyncService;

  @Override
  public void run(String... args) {
    log.info("Starting TimesheetSyncService...");

    log.info("Waiting 8 seconds for Users, Groups, and Job Codes to finish initial sync...");
    Thread.sleep(8000);   // 8 seconds delay

    if (timesheetRepository.count() == 0) {
      log.info("No timesheets found → Running initial 2-hour import");
      importLast2Hours();
    } else {
      log.info("Timesheets already exist → Starting normal schedule");
    }
  }

  /**
   * Initial load: last 2 hours
   */
  public void importLast2Hours() {
    // Ensure Users and JobCodes are up-to-date first
    userSyncService.incrementalUserSync();
    jobCodeSyncService.incrementalJobCodeSync();
    groupSyncService.incrementalGroupSync();

    OffsetDateTime startDate = OffsetDateTime.now().minusHours(2);

    Map<String, String> params = new HashMap<>();
    params.put("start_date", startDate.toLocalDate().toString()); // YYYY-MM-DD
    params.put("on_the_clock", "both");
    params.put("limit", "200");

    syncTimesheets(params, true); // true = initial load
  }

  /**
   * Runs every 120 seconds
   */
  @Scheduled(fixedRate = 120_000)
  public void incrementalSync() {
    log.info("Starting 2-minute incremental timesheet sync...");

    // Always make sure Users and JobCodes are current before processing timesheets
    userSyncService.incrementalUserSync();
    jobCodeSyncService.incrementalJobCodeSync();
    groupSyncService.incrementalGroupSync();

    OffsetDateTime lastModified = timesheetRepository.findMaxLastModified()
        .orElse(OffsetDateTime.now().minusHours(48));

    String modifiedSince = lastModified.withNano(0)
        .withOffsetSameInstant(java.time.ZoneOffset.UTC)
        .toString();

    Map<String, String> params = new HashMap<>();
    params.put("modified_since", modifiedSince);
    params.put("on_the_clock", "both");
    params.put("limit", "200");

    syncTimesheets(params, false); // false = incremental (check deletes)
  }

  private void syncTimesheets(Map<String, String> params, boolean isInitialLoad) {
    int page = 1;
    boolean hasMore = true;
    int processed = 0;

    while (hasMore) {
      params.put("page", String.valueOf(page));

      try {
        TimesheetResponse response = tsheetsClient.getTimesheets(params);

        if (response != null && response.getResults() != null
            && response.getResults().getTimesheets() != null) {

          var timesheets = response.getResults().getTimesheets().values();
          processed += timesheets.size();

          timesheets.forEach(dto -> {
            TimesheetEntity entity = mapper.toTimesheetEntity(dto);
            timesheetRepository.save(entity);
          });

          log.info("Saved {} timesheets from page {}", timesheets.size(), page);
        }

        hasMore = response != null ? response.getMore() : false;
        page++;
      } catch (Exception e) {
        log.error("Error syncing timesheets on page {}", page, e);
        break;
      }
    }

    if (!isInitialLoad) {
      checkForDeletedTimesheets();
    }

    log.info("Timesheet sync completed. Processed {} records.", processed);
  }

  /**
   * Check for deleted timesheets and soft-delete them locally
   */
  private void checkForDeletedTimesheets() {
    OffsetDateTime lastModified = timesheetRepository.findMaxLastModified()
        .orElse(OffsetDateTime.now().minusHours(2));

    String modifiedSince = lastModified.withNano(0)
        .withOffsetSameInstant(java.time.ZoneOffset.UTC)
        .toString();

    Map<String, String> params = new HashMap<>();
    params.put("modified_since", modifiedSince);
    params.put("limit", "200");

    int page = 1;
    boolean hasMore = true;
    int totalDeleted = 0;

    while (hasMore) {
      params.put("page", String.valueOf(page));

      try {
        TimesheetDeletedResponse response = tsheetsClient.getDeletedTimesheets(params);

        if (response != null && response.getResults() != null
            && response.getResults().getTimesheetsDeleted() != null) {

          Set<Long> deletedIds = response.getResults().getTimesheetsDeleted().keySet().stream()
              .map(Long::valueOf)
              .collect(Collectors.toSet());

          if (!deletedIds.isEmpty()) {
            int deletedCount = timesheetRepository.softDeleteByIds(deletedIds);
            totalDeleted += deletedCount;
            log.info("Soft-deleted {} timesheets on page {}", deletedCount, page);
          }
        }

        hasMore = response != null ? response.getMore() : false;
        page++;
      } catch (Exception e) {
        log.error("Error checking deleted timesheets on page {}", page, e);
        break;
      }
    }

    if (totalDeleted > 0) {
      log.info("Total soft-deleted timesheets in this run: {}", totalDeleted);
    }
  }
}