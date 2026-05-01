package com.example.come_backend_story.controller;

import com.example.come_backend_story.dto.OnClockLunchAlertDTO;
import com.example.come_backend_story.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final DashboardService dashboardService;

  @GetMapping("/on-clock-needing-lunch")
  public ResponseEntity<List<OnClockLunchAlertDTO>> getOnClockNeedingLunch() {
    List<OnClockLunchAlertDTO> alerts = dashboardService.getOnClockNeedingLunch();
    return ResponseEntity.ok(alerts);
  }
}