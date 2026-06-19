package com.securebank.api.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminAccountResponse {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private Long userId;
    private String ownerName;
    private String ownerEmail;
}
