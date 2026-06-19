package com.securebank.api.service;

import com.securebank.api.dto.AccountResponse;
import com.securebank.api.dto.AmountRequest;
import com.securebank.api.dto.TransactionResponse;
import com.securebank.api.dto.TransferRequest;
import com.securebank.api.dto.TransferResponse;
import com.securebank.api.entity.Account;
import com.securebank.api.entity.AppUser;
import com.securebank.api.entity.Transaction;
import com.securebank.api.entity.TransactionType;
import com.securebank.api.repository.AccountRepository;
import com.securebank.api.repository.AppUserRepository;
import com.securebank.api.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AppUserRepository appUserRepository;
    private final TransactionRepository transactionRepository;

    public AccountResponse createAccount(String email) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (accountRepository.existsByUser(user)) {
            throw new IllegalArgumentException("Account already exists for this user");
        }

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getBalance(),
                user.getFullName(),
                "Account created successfully"
        );
    }

    public AccountResponse getMyAccount(String email) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for this user"));

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                user.getFullName(),
                "Account fetched successfully"
        );
    }

    @Transactional
    public AccountResponse deposit(String email, AmountRequest request) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for this user"));

        BigDecimal updatedBalance = account.getBalance().add(request.getAmount());
        account.setBalance(updatedBalance);

        Account savedAccount = accountRepository.save(account);
        saveTransaction(savedAccount, TransactionType.DEPOSIT, request.getAmount(), savedAccount.getAccountNumber(), null);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getBalance(),
                user.getFullName(),
                "Deposit successful"
        );
    }

    @Transactional
    public AccountResponse withdraw(String email, AmountRequest request) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for this user"));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        BigDecimal updatedBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(updatedBalance);

        Account savedAccount = accountRepository.save(account);
        saveTransaction(savedAccount, TransactionType.WITHDRAW, request.getAmount(), savedAccount.getAccountNumber(), null);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getBalance(),
                user.getFullName(),
                "Withdraw successful"
        );
    }

    @Transactional
    public TransferResponse transfer(String email, TransferRequest request) {
        AppUser sender = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account senderAccount = accountRepository.findByUser(sender)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for this user"));

        if (senderAccount.getAccountNumber().equals(request.getReceiverAccountNumber())) {
            throw new IllegalArgumentException("Cannot transfer money to the same account");
        }

        Account receiverAccount = accountRepository.findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

        if (senderAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(request.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(request.getAmount()));

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        saveTransaction(
                senderAccount,
                TransactionType.TRANSFER_OUT,
                request.getAmount(),
                senderAccount.getAccountNumber(),
                receiverAccount.getAccountNumber()
        );
        saveTransaction(
                receiverAccount,
                TransactionType.TRANSFER_IN,
                request.getAmount(),
                senderAccount.getAccountNumber(),
                receiverAccount.getAccountNumber()
        );

        return new TransferResponse(
                senderAccount.getAccountNumber(),
                receiverAccount.getAccountNumber(),
                request.getAmount(),
                senderAccount.getBalance(),
                "Transfer successful"
        );
    }

    public List<TransactionResponse> getMyTransactions(String email) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for this user"));

        return transactionRepository.findByAccountOrderByCreatedAtDesc(account)
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getType().name(),
                        transaction.getAmount(),
                        transaction.getSourceAccountNumber(),
                        transaction.getTargetAccountNumber(),
                        transaction.getCreatedAt()
                ))
                .toList();
    }

    private void saveTransaction(
            Account account,
            TransactionType type,
            BigDecimal amount,
            String sourceAccountNumber,
            String targetAccountNumber
    ) {
        Transaction transaction = Transaction.builder()
                .account(account)
                .type(type)
                .amount(amount)
                .sourceAccountNumber(sourceAccountNumber)
                .targetAccountNumber(targetAccountNumber)
                .createdAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
    }

    private String generateAccountNumber() {
        String accountNumber;

        do {
            accountNumber = String.valueOf(ThreadLocalRandom.current().nextLong(1000000000L, 9999999999L));
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
}
