package com.securebank.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private String type;
    private BigDecimal amount;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private LocalDateTime createdAt;
}
