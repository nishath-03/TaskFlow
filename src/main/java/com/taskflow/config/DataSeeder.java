package com.taskflow.config;

import com.taskflow.entity.Project;
import com.taskflow.entity.Task;
import com.taskflow.entity.User;
import com.taskflow.enums.Priority;
import com.taskflow.enums.Role;
import com.taskflow.enums.TaskStatus;
import com.taskflow.repository.ProjectRepository;
import com.taskflow.repository.TaskRepository;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j  // gives us log.info() without writing a Logger field
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    // CommandLineRunner.run() is called automatically after Spring context starts
    // Perfect for seeding demo data
    @Override
    public void run(String... args) {
        // Only seed if database is empty (avoids duplicate errors on restart)
        if (userRepository.count() > 0) {
            log.info("Database already seeded, skipping...");
            return;
        }

        log.info("Seeding demo data...");

        // ── Create demo users ──────────────────────────────────────────────
        User admin = userRepository.save(User.builder()
                .name("Alice Admin")
                .email("admin@taskflow.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build());

        User manager = userRepository.save(User.builder()
                .name("Bob Manager")
                .email("manager@taskflow.com")
                .password(passwordEncoder.encode("manager123"))
                .role(Role.MANAGER)
                .build());

        User employee = userRepository.save(User.builder()
                .name("Carol Employee")
                .email("employee@taskflow.com")
                .password(passwordEncoder.encode("emp123"))
                .role(Role.EMPLOYEE)
                .build());

        // ── Create demo projects ───────────────────────────────────────────
        Project project1 = projectRepository.save(Project.builder()
                .name("Website Redesign")
                .description("Modernize the company website with new branding and UX")
                .createdBy(manager)
                .build());

        Project project2 = projectRepository.save(Project.builder()
                .name("Mobile App Launch")
                .description("Launch iOS and Android apps for customer self-service portal")
                .createdBy(admin)
                .build());

        // ── Create demo tasks (2 per status = 6 total, spread across both projects) ──
        taskRepository.save(Task.builder()
                .title("Design new homepage layout")
                .description("Create wireframes and mockups for the new homepage")
                .status(TaskStatus.TODO)
                .priority(Priority.HIGH)
                .project(project1)
                .assignedTo(employee)
                .createdBy(manager)
                .dueDate(LocalDate.now().plusDays(7))
                .build());

        taskRepository.save(Task.builder()
                .title("Set up CI/CD pipeline")
                .description("Configure GitHub Actions for automated testing and deployment")
                .status(TaskStatus.TODO)
                .priority(Priority.MEDIUM)
                .project(project2)
                .assignedTo(employee)
                .createdBy(admin)
                .dueDate(LocalDate.now().plusDays(14))
                .build());

        taskRepository.save(Task.builder()
                .title("Implement user authentication")
                .description("JWT-based login with role management")
                .status(TaskStatus.IN_PROGRESS)
                .priority(Priority.HIGH)
                .project(project2)
                .assignedTo(employee)
                .createdBy(manager)
                .dueDate(LocalDate.now().plusDays(3))
                .build());

        taskRepository.save(Task.builder()
                .title("Write API documentation")
                .description("Document all REST endpoints using OpenAPI/Swagger")
                .status(TaskStatus.IN_PROGRESS)
                .priority(Priority.LOW)
                .project(project1)
                .assignedTo(employee)
                .createdBy(admin)
                .dueDate(LocalDate.now().plusDays(10))
                .build());

        taskRepository.save(Task.builder()
                .title("Database schema design")
                .description("Design normalized schema for the task management system")
                .status(TaskStatus.DONE)
                .priority(Priority.HIGH)
                .project(project1)
                .assignedTo(employee)
                .createdBy(manager)
                .dueDate(LocalDate.now().minusDays(2))
                .build());

        taskRepository.save(Task.builder()
                .title("Requirements gathering")
                .description("Collect and document all stakeholder requirements")
                .status(TaskStatus.DONE)
                .priority(Priority.MEDIUM)
                .project(project2)
                .assignedTo(employee)
                .createdBy(admin)
                .dueDate(LocalDate.now().minusDays(5))
                .build());

        log.info("✅ Demo data seeded successfully!");
        log.info("   admin@taskflow.com    / admin123");
        log.info("   manager@taskflow.com  / manager123");
        log.info("   employee@taskflow.com / emp123");
    }
}
