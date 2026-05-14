package com.taskflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalTasks;
    private long todoCount;
    private long inProgressCount;
    private long doneCount;
    private long myTasksCount;  // tasks assigned to the current user
}
