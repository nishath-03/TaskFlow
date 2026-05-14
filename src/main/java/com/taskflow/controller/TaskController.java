package com.taskflow.controller;

import com.taskflow.dto.TaskDTO;
import com.taskflow.enums.TaskStatus;
import com.taskflow.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // GET /api/tasks — all authenticated users (filtering by role happens in service)
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks() {
        return ResponseEntity.ok(taskService.getTasks());
    }

    // POST /api/tasks — ADMIN + MANAGER only
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO dto) {
        return ResponseEntity.ok(taskService.createTask(dto));
    }

    // PUT /api/tasks/{id}/status — all roles (employee moves their own tasks)
    // Body: { "status": "IN_PROGRESS" }
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        TaskStatus status = TaskStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }

    // PUT /api/tasks/{id}/assign — ADMIN + MANAGER only
    // Body: { "userId": 3 }
    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskDTO> assignTask(
            @PathVariable Long id,
            @RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(taskService.assignTask(id, body.get("userId")));
    }

    // DELETE /api/tasks/{id} — ADMIN + MANAGER only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
