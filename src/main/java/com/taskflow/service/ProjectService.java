package com.taskflow.service;

import com.taskflow.dto.ProjectDTO;
import com.taskflow.entity.Project;
import com.taskflow.entity.User;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO createProject(ProjectDTO dto) {
        // Get the currently logged-in user's email from SecurityContext
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdBy(creator)
                .build();

        return toDTO(projectRepository.save(project));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    // Convert entity → DTO (never expose entity directly to API consumers)
    private ProjectDTO toDTO(Project p) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setCreatedByName(p.getCreatedBy().getName());
        return dto;
    }
}
