package com.taskflow.repository;

import com.taskflow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // JpaRepository gives us: findAll(), findById(), save(), deleteById() for free
}
