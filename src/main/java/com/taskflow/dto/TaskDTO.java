package com.taskflow.dto;

import com.taskflow.enums.Priority;
import com.taskflow.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDTO {
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private TaskStatus status;

    @NotNull
    private Priority priority;

    @NotNull
    private Long projectId;

    private Long assignedToId;      // nullable — task can be unassigned

    private String assignedToName;  // read-only, populated in response
    private String createdByName;   // read-only, populated in response

    private LocalDate dueDate;
}
