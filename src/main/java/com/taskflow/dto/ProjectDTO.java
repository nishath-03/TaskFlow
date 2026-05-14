package com.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectDTO {
    private Long id;

    @NotBlank
    private String name;

    private String description;
    private String createdByName;   // read-only, populated in response
}
