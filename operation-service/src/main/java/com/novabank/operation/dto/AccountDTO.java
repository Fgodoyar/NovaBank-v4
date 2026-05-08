package com.novabank.operation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountDTO(
        Long accountId,
        String accountNumber,
        String accountHolder,
        BigDecimal balance,
        LocalDateTime creationDate
) {}