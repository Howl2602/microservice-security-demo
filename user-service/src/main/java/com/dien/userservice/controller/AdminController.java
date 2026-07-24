package com.dien.userservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(
            @RequestHeader(value = "X-Authenticated-User", required = false) String username,
            @RequestHeader(value = "X-Authenticated-Role", required = false) String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "ADMIN role is required");
        }

        return Map.of(
                "message", "Admin access granted",
                "username", username == null ? "unknown" : username,
                "role", role,
                "service", "user-service"
        );
    }
}
