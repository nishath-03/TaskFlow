package com.taskflow.controller;

import com.taskflow.dto.DashboardStatsDTO;
import com.taskflow.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // GET /api/dashboard/stats — available to ALL authenticated roles
    // Role-specific filtering happens inside the service
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }
}
