package com.securebank.api.controller;

import com.securebank.api.dto.AdminAccountResponse;
import com.securebank.api.dto.AdminUserResponse;
import com.securebank.api.service.AdminService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public Map<String, String> getDashboard() {
        return Map.of("message", "Welcome to admin dashboard");
    }

    @GetMapping("/users")
    public List<AdminUserResponse> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/accounts")
    public List<AdminAccountResponse> getAllAccounts() {
        return adminService.getAllAccounts();
    }
}
