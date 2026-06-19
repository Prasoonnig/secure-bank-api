package com.securebank.api.service;

import com.securebank.api.dto.AdminAccountResponse;
import com.securebank.api.dto.AdminUserResponse;
import com.securebank.api.repository.AccountRepository;
import com.securebank.api.repository.AppUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AppUserRepository appUserRepository;
    private final AccountRepository accountRepository;

    public List<AdminUserResponse> getAllUsers() {
        return appUserRepository.findAll()
                .stream()
                .map(user -> new AdminUserResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole().name()
                ))
                .toList();
    }

    public List<AdminAccountResponse> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(account -> new AdminAccountResponse(
                        account.getId(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getUser().getId(),
                        account.getUser().getFullName(),
                        account.getUser().getEmail()
                ))
                .toList();
    }
}
