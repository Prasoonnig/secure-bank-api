package com.securebank.api.controller;

import com.securebank.api.dto.AccountResponse;
import com.securebank.api.dto.AmountRequest;
import com.securebank.api.dto.TransactionResponse;
import com.securebank.api.dto.TransferRequest;
import com.securebank.api.dto.TransferResponse;
import com.securebank.api.service.AccountService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(Authentication authentication) {
        AccountResponse response = accountService.createAccount(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponse> getMyAccount(Authentication authentication) {
        AccountResponse response = accountService.getMyAccount(authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountResponse> deposit(
            Authentication authentication,
            @Valid @RequestBody AmountRequest request
    ) {
        AccountResponse response = accountService.deposit(authentication.getName(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            Authentication authentication,
            @Valid @RequestBody AmountRequest request
    ) {
        AccountResponse response = accountService.withdraw(authentication.getName(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            Authentication authentication,
            @Valid @RequestBody TransferRequest request
    ) {
        TransferResponse response = accountService.transfer(authentication.getName(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getMyTransactions(Authentication authentication) {
        List<TransactionResponse> response = accountService.getMyTransactions(authentication.getName());
        return ResponseEntity.ok(response);
    }
}
