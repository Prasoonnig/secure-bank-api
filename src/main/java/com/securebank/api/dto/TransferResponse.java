package com.securebank.api.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransferResponse {

    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal amount;
    private BigDecimal senderBalance;
    private String message;
}
