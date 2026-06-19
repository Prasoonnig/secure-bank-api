package com.securebank.api.repository;

import com.securebank.api.entity.Account;
import com.securebank.api.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountOrderByCreatedAtDesc(Account account);
}
