package com.taskflow.service;

import com.taskflow.dto.DashboardStatsDTO;
import com.taskflow.entity.User;
import com.taskflow.enums.Role;
import com.taskflow.enums.TaskStatus;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public DashboardStatsDTO getStats() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long totalTasks = taskRepository.count();
        long todoCount = taskRepository.countByStatus(TaskStatus.TODO);
        long inProgressCount = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long doneCount = taskRepository.countByStatus(TaskStatus.DONE);

        // myTasksCount: for EMPLOYEE = their assigned tasks; for others = total
        long myTasksCount;
        if (currentUser.getRole() == Role.EMPLOYEE) {
            myTasksCount = taskRepository.countByAssignedTo(currentUser);
        } else {
            myTasksCount = totalTasks;
        }

        return new DashboardStatsDTO(totalTasks, todoCount, inProgressCount, doneCount, myTasksCount);
    }
}
