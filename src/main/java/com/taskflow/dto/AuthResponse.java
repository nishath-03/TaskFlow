package com.taskflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;   // JWT string
    private String name;    // user's display name
    private String email;
    private String role;    // "ADMIN" | "MANAGER" | "EMPLOYEE"
}
