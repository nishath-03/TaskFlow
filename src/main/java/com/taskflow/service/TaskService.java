package com.taskflow.service;

import com.taskflow.dto.TaskDTO;
import com.taskflow.entity.Project;
import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import com.taskflow.enums.Role;
import com.taskflow.enums.TaskStatus;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    // ADMIN/MANAGER: see all tasks | EMPLOYEE: see only their tasks
    public List<TaskDTO> getTasks() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> tasks;
        if (currentUser.getRole() == Role.EMPLOYEE) {
            // EMPLOYEE can only see tasks assigned to them
            tasks = taskRepository.findByAssignedTo(currentUser);
        } else {
            // ADMIN and MANAGER see everything
            tasks = taskRepository.findAll();
        }

        return tasks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TaskDTO createTask(TaskDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignee = null;
        if (dto.getAssignedToId() != null) {
            assignee = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus() != null ? dto.getStatus() : TaskStatus.TODO)
                .priority(dto.getPriority())
                .project(project)
                .assignedTo(assignee)
                .createdBy(creator)
                .dueDate(dto.getDueDate())
                .build();

        return toDTO(taskRepository.save(task));
    }

    // Update status only — used by EMPLOYEE to move their own tasks
    public TaskDTO updateStatus(Long id, TaskStatus newStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(newStatus);
        return toDTO(taskRepository.save(task));
    }

    // Assign task to a user — MANAGER + ADMIN
    public TaskDTO assignTask(Long id, Long userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setAssignedTo(user);
        return toDTO(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO toDTO(Task t) {
        TaskDTO dto = new TaskDTO();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getDescription());
        dto.setStatus(t.getStatus());
        dto.setPriority(t.getPriority());
        dto.setProjectId(t.getProject().getId());
        dto.setDueDate(t.getDueDate());
        if (t.getAssignedTo() != null) {
            dto.setAssignedToId(t.getAssignedTo().getId());
            dto.setAssignedToName(t.getAssignedTo().getName());
        }
        dto.setCreatedByName(t.getCreatedBy().getName());
        return dto;
    }
}
