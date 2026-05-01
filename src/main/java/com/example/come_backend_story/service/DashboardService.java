package com.example.come_backend_story.service;

import com.example.come_backend_story.dto.OnClockLunchAlertDTO;
import com.example.come_backend_story.entity.GroupEntity;
import com.example.come_backend_story.entity.TimesheetEntity;
import com.example.come_backend_story.entity.UserEntity;
import com.example.come_backend_story.repository.GroupRepository;
import com.example.come_backend_story.repository.TimesheetRepository;
import com.example.come_backend_story.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

  private final TimesheetRepository timesheetRepository;
  private final UserRepository userRepository;
  private final GroupRepository groupRepository;   // ← Added

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  public List<OnClockLunchAlertDTO> getOnClockNeedingLunch() {
    List<OnClockLunchAlertDTO> alerts = new ArrayList<>();

    List<TimesheetEntity> onClockList = timesheetRepository.findByOnTheClockTrue();

    for (TimesheetEntity currentPunch : onClockList) {
      UserEntity user = userRepository.findById(currentPunch.getUserId()).orElse(null);
      if (user == null) continue;

      // Lookup group name from groups table
      String groupName = groupRepository.findById(user.getGroupId())
          .map(GroupEntity::getName)
          .orElse("Unknown Group");

      double paidHours = calculateCurrentPaidHours(currentPunch);   // placeholder for now

      boolean hasTakenLunch = hasTakenLunchToday(currentPunch.getUserId(), currentPunch.getStartTime());

      int lunchNeeded = determineNextLunchNeeded(paidHours, hasTakenLunch);
      if (lunchNeeded == 0) continue;

      int minutesLeft = calculateMinutesUntilLunchDeadline(paidHours, lunchNeeded);

      String fullName = user.getFirstName() + " " + user.getLastName();

      alerts.add(OnClockLunchAlertDTO.builder()
          .userId(user.getId())
          .fullName(fullName)
          .groupName(groupName)                          // ← Now has real name
          .currentPaidHours(Math.round(paidHours * 100.0) / 100.0)
          .lunchNumberNeeded(lunchNeeded)
          .minutesUntilDeadline(minutesLeft)
          .deadlineTime(OffsetDateTime.now().plusMinutes(minutesLeft).format(TIME_FORMATTER))
          .clockInTime(currentPunch.getStartTime())
          .tz(currentPunch.getTz())
          .tzStr(currentPunch.getTzStr())
          .hasTakenLunchToday(hasTakenLunch)
          .build());
    }

    return alerts;
  }

  // ====================== CORE LOGIC ======================

  private double calculateCurrentPaidHours(TimesheetEntity currentPunch) {
    // For now: simple duration from clock-in to now
    // Also does not add together time sheets
    // TODO: Full commute logic (subtract first + last hour of jobcode 1160965)
    OffsetDateTime now = OffsetDateTime.now(currentPunch.getStartTime().getOffset());
    return java.time.Duration.between(currentPunch.getStartTime(), now).toMinutes() / 60.0;
  }

  private boolean hasTakenLunchToday(Long userId, OffsetDateTime todayStart) {
    // TODO: Full implementation - check for unpaid break (11431907) >= 30 min OR 30+ min gaps
    // For now returning false so the endpoint works
    return false;
  }

  private int determineNextLunchNeeded(double paidHours, boolean hasTakenLunch) {
    if (paidHours < 5.0) return hasTakenLunch ? 2 : 1;
    if (paidHours < 10.0) return hasTakenLunch ? 3 : 2;
    if (paidHours < 15.0) return 3;
    return 0;
  }

  private int calculateMinutesUntilLunchDeadline(double paidHours, int lunchNumber) {
    double target = switch (lunchNumber) {
      case 1 -> 5.0;
      case 2 -> 10.0;
      case 3 -> 15.0;
      default -> 999.0;
    };
    double hoursLeft = target - paidHours;
    return (int) Math.max(0, Math.ceil(hoursLeft * 60));
  }
}