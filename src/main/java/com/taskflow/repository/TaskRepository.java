package com.taskflow.repository;

import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import com.taskflow.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // EMPLOYEE: fetch only their own tasks
    List<Task> findByAssignedTo(User user);

    // Dashboard stats — count tasks by status
    long countByStatus(TaskStatus status);

    // Dashboard — employee's task count by status
    long countByAssignedToAndStatus(User user, TaskStatus status);

    // Count tasks assigned to a specific user (for myTasksCount)
    long countByAssignedTo(User user);
}
