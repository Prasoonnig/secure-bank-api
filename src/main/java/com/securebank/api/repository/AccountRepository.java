package com.securebank.api.repository;

import com.securebank.api.entity.Account;
import com.securebank.api.entity.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUser(AppUser user);

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByUser(AppUser user);

    boolean existsByAccountNumber(String accountNumber);
}
