package com.taskflow.service;

import com.taskflow.entity.User;
import com.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ADMIN-only: return all users (used for the Users management page)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
