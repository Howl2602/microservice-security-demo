package com.dien.userservice.controller;

import com.dien.userservice.entity.User;
import com.dien.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(
            @RequestHeader(value = "X-Authenticated-Role", required = false) String role) {
        requireAdmin(role);
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-Authenticated-User", required = false) String username,
            @RequestHeader(value = "X-Authenticated-Role", required = false) String role) {

        User user = userService.getUserById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!"ADMIN".equalsIgnoreCase(role)
                && (username == null || !username.equals(user.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return user;
    }

    @PostMapping
    public User createUser(
            @RequestBody User user,
            @RequestHeader(value = "X-Authenticated-Role", required = false) String role) {
        requireAdmin(role);
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id,
            @RequestHeader(value = "X-Authenticated-Role", required = false) String role) {
        requireAdmin(role);
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestBody User user,
            @RequestHeader(value = "X-Authenticated-Role", required = false) String role) {
        requireAdmin(role);
        return userService.updateUser(id, user);
    }

    private void requireAdmin(String role) {
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ADMIN role is required");
        }
    }
}
