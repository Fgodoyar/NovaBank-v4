package com.novabank.operation.customer;

import com.novabank.operation.dto.AccountDTO;
import com.novabank.operation.dto.CreateTransactionRequest;
import com.novabank.operation.dto.TransactionDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountServiceFallback implements AccountServiceClient {

    @Override
    public AccountDTO getAccountById(Long id) {
        return null;
    }

    @Override
    public AccountDTO getAccountByNumber(String accountNumber) {
        return null;
    }

    @Override
    public AccountDTO updateBalance(Long id, BigDecimal amount) {
        return null;
    }

    @Override
    public TransactionDTO createTransaction(Long accountId, CreateTransactionRequest request) {
        return null;
    }
}